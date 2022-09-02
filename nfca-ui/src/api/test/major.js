import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

//查询综测班级用户列表
export function TestUser(query) {
  return request({
    url: '/test/major/list',
    method: 'get',
    params: query
  })
}

// 查询用户详细
export function getTestUser(userId) {
  return request({
    url: '/test/major/' + parseStrEmpty(userId),
    method: 'get'
  })
}


/**
 * 驳回学生申请
 * @param id 唯一标识
 * @param no 学生学号
 * @param reason 驳回原因
 * @param operator 操作员
 * @returns {*}
 */
export function backUser(id,no,reason,operator) {
  const data = {
    id,
    no,
    reason,
    operator
  }
  return request({
    url: '/test/major/backUser',
    method: 'put',
    data: data
  })
}


/*
*
 * 批量通过学生申请
 * nos 为所选学生no数组
 * @param nos
 * @param operator 操作员
 * @returns {*}
 */

export function accessUser(nos,operator) {
  const data = {
    nos,
    operator,

  }
  return request({
    url: '/test/major/access',
    method: 'put',
    data: data
  })
}

/**
 * 通过单个学生
 * @param no 学生学号
 * @param operator 操作人员
 * @returns {*}
 */
export function accessUser2(no,operator) {
  const data = {
    no,
    operator
  }
  return request({
    url: '/test/major/accessUser2',
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
    url: '/test/major/getAwards',
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
    url: '/test/major/updateScore',
    method: 'put',
    data: data
  })
}
/*
* 推送接口
* */
export function pushmsg(openId,Reason){
  const data={
    openId,Reason
  }
  return request({
    url:'/push',
    method:'post',
    data:data
  })
}
