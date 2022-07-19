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

// 驳回学生申请
export function backUser(userId,status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/test/class/' + userId,
    method: 'get',
    data: data
  })
}

// 通过学生申请
export function accessUser(userId,status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/test/class/' + userId,
    method: 'put',
    data: data
  })
}
