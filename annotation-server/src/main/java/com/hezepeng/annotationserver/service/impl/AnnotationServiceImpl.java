package com.hezepeng.annotationserver.service.impl;

import com.hezepeng.annotationserver.common.Const;
import com.hezepeng.annotationserver.common.ServerResponse;
import com.hezepeng.annotationserver.dao.AnnotationRepository;
import com.hezepeng.annotationserver.dao.UserRepository;
import com.hezepeng.annotationserver.entity.News;
import com.hezepeng.annotationserver.entity.NewsAnnotation;
import com.hezepeng.annotationserver.entity.User;
import com.hezepeng.annotationserver.entity.UserTask;
import com.hezepeng.annotationserver.entity.bo.AnnotationTask;
import com.hezepeng.annotationserver.entity.bo.NewsBo;
import com.hezepeng.annotationserver.service.AnnotationService;
import com.hezepeng.annotationserver.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/19 16:56
 */
@Service
public class AnnotationServiceImpl implements AnnotationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ServerResponse<List<News>> getNewsAnnotationList(HttpServletRequest request) {
        try {
            List<News> data = annotationRepository.findAllAnnotationNewsList();
            return ServerResponse.createBySuccess(data);

        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse<List<News>> getTaskListByUsername(HttpServletRequest request) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            User user = userRepository.selectUserByUsername(username);
            if (user.getRole().contains(Const.ADMIN_ROLE)) {
                return ServerResponse.createBySuccess(annotationRepository.findAllTaskList());
            }
            return ServerResponse.createBySuccess(annotationRepository.findTaskListByUsername(username));
        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse<News> getOneNewsAnnotationById(String id) {
        try {
            News data = annotationRepository.findOneSimpleNewsById(id);
            return ServerResponse.createBySuccess(data);

        } catch (Exception ex) {
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse<List<NewsBo>> getAnnotationIdList(HttpServletRequest request) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            List<NewsBo> ids = new LinkedList<>();
            List<News> objectIds;
            objectIds = annotationRepository.findAllAnnotationIdByUsername(username);
            objectIds.forEach(item -> {
                int userIndex = item.getUsers().indexOf(username);
                if (userIndex >= 0) {
                    int newsState = item.getNews_annotation_done().get(userIndex) == null ? 0 : 1;
                    ids.add(new NewsBo(item.get_id().toHexString(), newsState));
                }
            });
            // 先按状态排序 标注过的排前面 再按id排序
            Comparator<NewsBo> byState = Comparator.comparing(NewsBo::getState).reversed();
            Comparator<NewsBo> byId = Comparator.comparing(NewsBo::getId);
            ids.sort(byState.thenComparing(byId));
            return ServerResponse.createBySuccess(ids);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse addNewsAnnotation(HttpServletRequest request, NewsAnnotation annotation) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            String id = annotation.get_id();
            boolean isUpdate = false;
            News news = annotationRepository.findOneNewsById(id);
            int index = news.getUsers().indexOf(username);
            news.setNews_state(1);
            System.out.println(news);
            // 检查是更新还是新增
            LinkedList<Boolean> done = news.getNews_annotation_done();
            if (done.get(index) != null) {
                // 更新标注
                isUpdate = true;
            } else {
                // 新增标注，设置成当前用户已标注、未校验
                done.set(index, false);
            }
            // 创建时间
            LinkedList<Date> date = news.getNews_annotation_create_time();
            date.set(index, new Date());
            // 情感
            LinkedList<LinkedList<String>> emotion = news.getNews_emotion();
            emotion.set(index, annotation.getNews_emotion());
            //立场
            LinkedList<String> position = news.getNews_position();
            position.set(index, annotation.getNews_position());
            //主题
            LinkedList<String> subject = news.getNews_subject();
            subject.set(index, annotation.getNews_subject());
            // 类型
            LinkedList<String> type = news.getNews_type();
            type.set(index, annotation.getNews_type());
            // 是否中国相关
            LinkedList<Boolean> aboutChina = news.getNews_about_china();
            aboutChina.set(index, annotation.getNews_about_china());
            // 情感依据
            LinkedList<String> basis = news.getNews_emotion_basis();
            basis.set(index, annotation.getNews_emotion_basis());

            // 如果这篇新闻的标注者都标注完了 就把状态改成待校验
            long doneCount = news.getNews_annotation_done().stream().filter(Objects::nonNull).count();
            if (news.getUsers().size() - 1 == doneCount && news.getNews_state().equals(1)) {
                news.setNews_state(2);
            }
            mongoTemplate.save(news);
            if (isUpdate) {
                return ServerResponse.createBySuccessMessage("标注已更新");
            } else {
                return ServerResponse.createBySuccessMessage("标注已添加");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ServerResponse.createByErrorMessage(ex.getMessage());
        }
    }

    @Override
    public ServerResponse createTask(AnnotationTask task) {
        List<UserTask> userTaskList = task.getUserTaskList();
        Query query = new Query();
        query.addCriteria(Criteria.where("news_state").is(null));
        query.limit(task.getNewsCount());
        List<News> newsList = annotationRepository.findNewsListByState(null, 0, task.getNewsCount());
        System.out.println(newsList.size());
        Queue<String> userQueue = new LinkedList<>();

        userTaskList.forEach(userTask -> {
            for (int i = 0; i < userTask.getTaskCount(); i++) {
                userQueue.offer(userTask.getUsername());
            }
        });
        int newsIndex = 0;
        String username = null;
        while ((username = userQueue.poll()) != null) {
            newsIndex %= newsList.size();
            if (newsList.get(newsIndex).getUsers() == null) {
                newsList.get(newsIndex).setUsers(new LinkedList<>());
            }
            LinkedList<String> users = newsList.get(newsIndex).getUsers();
            users.add(username);
            newsIndex++;
        }
        newsList.forEach(news -> {
            news.getUsers().add("FinalCheck");
            annotationRepository.initField(news);
        });
        return ServerResponse.createBySuccessMessage("任务初始化成功");
    }

    @Override
    public ServerResponse checkAnnotation() {
        List<News> newsList = annotationRepository.findNewsListByState(2, null, null);
        int complete = newsList.size();
        AtomicInteger pass = new AtomicInteger();
        AtomicInteger fail = new AtomicInteger();
        AtomicInteger exception = new AtomicInteger();
        newsList.forEach(news -> {
            try {
                boolean needCheck = false;
                int userCount = news.getUsers().size();
                HashSet set = null;
                // 判断情感标注
                HashMap<String, Integer> map = new LinkedHashMap<>();
                news.getNews_emotion().forEach(emotions -> {
                    if (emotions != null) {
                        emotions.forEach(emotion -> {
                            if (map.containsKey(emotion)) {
                                map.put(emotion, map.get(emotion) + 1);
                            } else {
                                map.put(emotion, 1);
                            }
                        });
                    }
                });
                List<Map.Entry<String, Integer>> entryList = new ArrayList<>(map.entrySet());
                entryList.sort(Map.Entry.comparingByValue());

                int firstEmotionCount = entryList.get(0).getValue();
                int secondEmotionCount = entryList.get(1).getValue();
                if (news.getNews_emotion().get(userCount - 1) == null) {
                    news.getNews_emotion().set(userCount - 1, new LinkedList<>());
                }
                if (firstEmotionCount == (userCount - 1) && firstEmotionCount > 1) {
                    news.getNews_emotion().get(userCount - 1).add(String.valueOf(entryList.get(0).getKey()));
                    // 如果标注none的最多 并且第二多的至少有大于一个 小于userCount个 就保留 否则需要人工校验
                }
                if (secondEmotionCount == (userCount - 1) && secondEmotionCount > 1) {
                    news.getNews_emotion().get(userCount - 1).add(String.valueOf(entryList.get(1).getKey()));
                } else if (entryList.get(0).getKey().equals("none")) {
                    needCheck = true;
                }

                //判断立场
                set = new HashSet(news.getNews_position());
                set.remove(null);
                if (set.size() != 1) {
                    fail.incrementAndGet();
                    needCheck = true;
                } else {
                    pass.incrementAndGet();
                    news.getNews_position().set(news.getUsers().size() - 1, (String) set.iterator().next());
                }

                // 判断主题分类
                set = new HashSet<>(news.getNews_subject());
                set.remove(null);
                if (set.size() != 1) {
                    fail.incrementAndGet();
                    needCheck = true;
                } else {
                    pass.incrementAndGet();
                    news.getNews_subject().set(news.getUsers().size() - 1, (String) set.iterator().next());
                }

                // 判断新闻类型
                set = new HashSet<>(news.getNews_type());
                set.remove(null);
                if (set.size() != 1) {
                    fail.incrementAndGet();
                    needCheck = true;
                } else {
                    pass.incrementAndGet();
                    news.getNews_type().set(news.getUsers().size() - 1, (String) set.iterator().next());
                }

                // 判断是否中国
                set = new HashSet<>(news.getNews_about_china());
                set.remove(null);
                if (set.size() != 1) {
                    needCheck = true;
                } else {
                    news.getNews_about_china().set(news.getUsers().size() - 1, (Boolean) set.iterator().next());
                }

                if (needCheck) {
                    news.setNews_state(4);
                } else {
                    news.setNews_state(10);
                }
                annotationRepository.updateNews(news);
                pass.incrementAndGet();
            } catch (Exception ex) {
                exception.incrementAndGet();
            }

        });
        Map<String, Integer> data = new HashMap<>(4);
        data.put("complete", complete);
        data.put("pass", pass.get());
        data.put("fail", fail.get());
        data.put("exception", exception.get());
        return ServerResponse.createBySuccess(data);
    }

    @Override
    public ServerResponse createFoundationSkill(HttpServletRequest request, Integer skill) {
        System.out.println(skill);
        if (skill == null || skill < 0 || skill >= 4) {
            return ServerResponse.createByErrorMessage("基础选择错误,请重新勾选!");
        }
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            User user = userRepository.selectUserByUsername(username);
            user.setFoundationSkill(skill);
            user.setMachineLearningTaskState(1);
            userRepository.updateUser(user);
            return ServerResponse.createBySuccessMessage("保存成功!");
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("发生异常");
        }
    }

    @Override
    public ServerResponse getOrSetMachineLearningModel(HttpServletRequest request) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            Integer machineNo = new Random().nextInt(4);
            User user = userRepository.selectUserByUsername(username);
            user.setMachineLearningModel(machineNo);
            if (user.getMachineLearningTaskState() != null && user.getMachineLearningTaskState() < 2) {
                return ServerResponse.createByErrorMessage("请先完成第一步操作");

            }
            userRepository.updateUser(user);
            return ServerResponse.createBySuccess(machineNo);
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("发生异常");
        }
    }

    @Override
    public ServerResponse getAndUpdateMachineLearningTaskState(HttpServletRequest request) {
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            User user = userRepository.selectUserByUsername(username);
            if (user.getMachineLearningTaskState() != null && user.getMachineLearningTaskState() < 2) {
                int count = annotationRepository.getUserUndoTaskCount(username);
                if (count > 0) {
                    return ServerResponse.createByErrorMessage("请先完成标注任务再进入下一步");
                }
                user.setMachineLearningTaskState(4);
                userRepository.updateUser(user);
                return ServerResponse.createBySuccess("恭喜你已经完成了数据标注任务", user);
            } else {
                return ServerResponse.createByErrorMessage("请先完成前置操作");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.createByErrorMessage("发生异常");
        }
    }

    @Override
    public ServerResponse createModelInfo(HttpServletRequest request, User data) {
        if (StringUtils.isEmpty(data.getModelName()) || StringUtils.isEmpty(data.getModelIntroduction())) {
            return ServerResponse.createByErrorMessage("请填写必要的模型名称和介绍");
        } else if (StringUtils.isEmpty(data.getModelFileUrl())) {
            return ServerResponse.createByErrorMessage("请先上传文件再提交");
        }
        try {
            String username = TokenUtil.getUsernameByRequest(request);
            User user = userRepository.selectUserByUsername(username);
            if (user.getMachineLearningTaskState() < 4) {
                return ServerResponse.createByErrorMessage("请先完成前置操作");
            }
            user.setModelName(data.getModelName());
            user.setModelIntroduction(data.getModelIntroduction());
            user.setModelFileUrl(data.getModelFileUrl());
            user.setMachineLearningTaskState(5);
            userRepository.updateUser(user);
            return ServerResponse.createBySuccessMessage("提交成功!");
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage("发生异常");
        }
    }
}
