// pages/downword/downword.js
import {request} from"../../requst/index.js";
import regeneratorRuntime from"../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    result:[],
    name:'',
    http:'http://47.96.86.132:8080/csom/file/download?name='
  },
  /**
   * 生命周期函数--监听页面加载
   */
   

  onLoad: function (options) {
    this.getfilelist()
  },
  async getfilelist(){
    const res = await request({url:'/file/list'});
    const {result} = res.data
    this.setData({
      result
    })
  },
 down(e){
  console.log(e)
  const {name} = e.currentTarget.dataset
  const {http} = this.data
  wx.showModal({
    title: '提示',
    content: '请点击复制按钮并粘贴到浏览器下载',
    success (res) {
      if (res.confirm) {
        wx.setClipboardData({
          data:http +name,
          success (res) {
            wx.getClipboardData({
              success (res) {
              }
            })
          }
        })
      } else if (res.cancel) {
        wx.showToast({
          title: '取消复制',
          icon: 'none',
          image: '',
          duration: 1500,
          mask: false,
          
        });
          
      }
    }
  })
 },
    
    
   


 
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})