import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

//查询综测班级用户列表
export function TestUser(query) {
  return request({
    url: '/test/class/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getTestUser(userId) {
  return request({
    url: '/test/class/' + parseStrEmpty(userId),
    method: 'get'
  })
}
