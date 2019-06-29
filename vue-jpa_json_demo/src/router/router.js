import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ArticleList from '@/components/ArticleList'
import BlogDetail from '@/components/BlogDetail'
import PostArticle from '@/components/PostArticle'
import CateMana from '@/components/CateMana'
import UserMana from '@/components/UserMana'

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [
        {
            path: '/',
            name: '登录',
            component: Login,
            meta: {
                menuShow: false,
            }
        }, {
            path: '/home',
            component: Home,
            name: 'articleMana',
            meta: {
                title: '文章管理',
                iconCls: 'fa fa-file-text-o',
                menuShow: true,
            },
            children: [
                {
                    path: '/articleList',
                    name: 'articleList',
                    component: ArticleList,
                    meta: {
                        title: '文章列表',
                        menuShow: true,
                        keepAlive: true
                    }
                }, {
                    path: '/postArticle',
                    name: 'postArticle',
                    component: PostArticle,
                    meta: {
                        title: '发表文章',
                        menuShow: true,
                        keepAlive: false
                    }
                }, {
                    path: '/blogDetail',
                    name: 'blogDetail',
                    component: BlogDetail,
                    meta: {
                        title: '博客详情',
                        menuShow: false,
                        keepAlive: false
                    }
                }, {
                    path: '/editBlog',
                    name: 'editBlog',
                    component: PostArticle,
                    meta: {
                        title: '编辑博客',
                        menuShow: false,
                        keepAlive: false
                    }
                }
            ]
        }, {
            path: '/home',
            component: Home,
            name: 'userMana',
            meta: {
                title: '用户管理',
                iconCls: 'fa fa-user-o',
                menuShow: true,
            },
            children: [
                {
                    path: '/userMana',
                    name: 'user',
                    component: UserMana,
                    meta: {
                        title: '用户管理',
                        iconCls: 'fa fa-user-o',
                        menuShow: true
                    }
                }
            ]
        }, {
            path: '/home',
            component: Home,
            name: 'cateMana',
            meta: {
                title: '栏目管理',
                iconCls: 'fa fa-reorder',
                menuShow: true,
            },
            children: [
                {
                    path: '/category', 
                    name: 'category',
                    component: CateMana,
                    meta: {
                        title: '栏目管理',
                        iconCls: 'fa fa-reorder',
                        menuShow: true
                    }
                }
            ]
        }, {
            path: '/home',
            component: Home,
            name: 'dataStat',
            meta: {
                title: '数据统计',         
                iconCls: 'fa fa-bar-chart',
                menuShow: true,
            },
            children: [
                {
                    path: '/charts',
                    name: 'charts',                   
                    //component: DataCharts,
                    meta: {
                        title: '数据统计',
                        iconCls: 'fa fa-bar-chart',
                        menuShow: true                       
                    }
                }
            ]
        }
    ]
})