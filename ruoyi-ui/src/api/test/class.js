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


/**
 * 驳回学生申请
 * @param no 学生学号
 * @param reason 驳回原因
 * @returns {*}
 */
export function backUser(no,reason) {
  const data = {
    no,
    reason,
  }
  return request({
    url: '/test/class/backUser',
    method: 'put',
    data: data
  })
}

/**
 * 批量通过学生申请
 * nos 为所选学生no数组
 * @param nos
 * @returns {*}
 */
export function accessUser(nos) {
  const data = {
    nos,
  }
  return request({
    url: '/test/class/' + parseStrEmpty(nos),
    method: 'put',
  })
}


/**
 * 通过单个学生
 * @param no 学生学号
 * @returns {*}
 */
export function accessUser2(no) {
  const data = {
    no,
  }
  return request({
    url: '/test/class/accessUser2',
    method: 'put',
    data: data
  })
}

/**
 * 查询奖项内容
 * @param no 学号
 * @param module 奖项模块
 */
export function getAwards(no,module) {
  const data = {
    no,
    module,
  }
  return request({
    url: '/test/class/getAwards',
    method: 'put',
    data: data
  })
}

