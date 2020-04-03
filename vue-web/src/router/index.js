import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

// 所有权限通用路由表
// 如首页和登录页和一些不用权限的公用页面
export const constantRouterMap = [
  {
    path: '/login',
    component: () => import('@/views/login/Login'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/home/Home'),
        meta: { title: '机器学习任务平台', icon: 'dashboard' }
      }
    ]
  }
  // {
  //   path: '/example',
  //   component: Layout,
  //   redirect: '/example/table',
  //   name: 'Example',
  //   meta: {title: 'Example'},
  //   children: [
  //     {
  //       path: 'table',
  //       name: 'Table',
  //       component: () => import('@/views/home/Home'),
  //       meta: {title: 'Table'}
  //     },
  //     {
  //       path: 'tree',
  //       name: 'Tree',
  //       component: () => import('@/views/home/Home'),
  //       meta: {title: 'Tree'}
  //     }
  //   ]
  // }
]

// 异步挂载的路由
// 动态需要根据权限加载的路由表
export const asyncRouterMap = [
  // 文章模块
  {
    path: '/annotation',
    component: Layout,
    name: 'Annotation',
    redirect: '/annotation/list',
    alwaysShow: true,
    meta: {
      title: '标注管理',
      icon: 'nested',
      role: ['admin', 'editor']
    },
    children: [
      {
        path: 'new',
        component: () => import('@/views/annotation/NewAnnotation'),
        name: 'NewAnnotation',
        meta: {
          role: ['admin', 'editor'],
          title: '开始标注',
          icon: 'form',
          noCache: true
          // 页面需要的权限
        }
      },
      {
        path: 'list',
        component: () => import('@/views/annotation/AnnotationTaskList'),
        name: 'AnnotationTaskList',
        meta: {
          role: ['admin'],
          title: '标注列表',
          icon: 'table',
          noCache: true
          // 页面需要的权限
        }
      }
    ]
  },

  // 用户模块
  {
    path: '/user',
    component: Layout,
    name: 'User',
    redirect: '/user/list',
    alwaysShow: true,
    meta: {
      title: '用户管理',
      icon: 'nested',
      role: ['admin']
    },
    children: [
      {
        path: 'new',
        component: () => import('@/views/user/AddUser'),
        name: 'NewUser',
        meta: {
          role: ['admin'],
          title: '新建用户',
          icon: 'form',
          noCache: true
          // 页面需要的权限
        }
      },
      {
        path: 'list',
        component: () => import('@/views/user/UserList'),
        name: 'UserList',
        meta: {
          role: ['admin'],
          title: '用户列表',
          icon: 'table',
          noCache: true
          // 页面需要的权限
        }
      }
    ]
  },

  // 用户模块
  {
    path: '/task',
    component: Layout,
    name: 'Task',
    redirect: '/task/new',
    alwaysShow: true,
    meta: {
      title: '任务管理',
      icon: 'nested',
      role: ['admin','editor']
    },
    children: [
      {
        path: 'new',
        component: () => import('@/views/task/NewTask'),
        name: 'NewTask',
        meta: {
          role: ['admin'],
          title: '新建分配',
          icon: 'tree',
          noCache: true
          // 页面需要的权限
        }
      },
      {
        path: '/machine-learning/new',
        component: () => import('@/views/task/NewMachineLearningTask'),
        name: 'NewMachineLearningTask',
        meta: {
          role: ['admin', 'editor'],
          title: '机器学习任务',
          icon: 'tree',
          noCache: true
          // 页面需要的权限
        }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
