import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

//查询综测院系用户列表
export function TestUser(query) {
  return request({
    url: '/test/institute',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getTestUser(userId) {
  return request({
    url: '/test/institute/' + parseStrEmpty(userId),
    method: 'get'
  })
}
