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


/**
 * 驳回学生申请
 * @param no 学生学号
 * @param id 图片唯一标识
 * @param reason 驳回原因
 * @returns {*}
 */
export function backUser(id,no,reason) {
  const data = {
    id,
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
export function getAwards(no) {
  const data = {
    no,
  }
  return request({
    url: '/test/class/getAwards',
    method: 'post',
    data: data
  })
}

/**
 * 修改各个小点的成绩
 * @param id 图片的id,唯一标识
 * @param score 图片的id对应的小点成绩(各个小点的成绩)
 * @returns {*}
 */
export function updateScore(id,score) {
  const data = {
    id,
    score,
  }
  return request({
    url: '/test/class/updateScore',
    method: 'put',
    data: data
  })
}
/*
* 导出功能
* */
export function exportUser(query) {
  return request({
    url:'/test/class/export',
    method:'get',
    params:query
  })
}

