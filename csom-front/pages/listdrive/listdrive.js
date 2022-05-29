import {request} from"../../requst/index.js";
import regeneratorRuntime from"../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    //技术培训的值
    listTrain:[],
    page:{
      currPage:'1',//当前页
      totalPage:'',//总页数
    },
  },

  /**
   * 生命周期函数--监听页面加载
   */
   async listTrain(curr){
     try {
      const res = await request({url:'/article/list',method:"GET",data:{page:curr,type:1}});
      console.log(res)
      let {list} = res.data.result
      let {currPage} =res.data.result
      let {totalPage} =res.data.result
      if(list.length == 0){
        wx.showToast({
          title: '该页没有文章',
          icon: 'error',
          duration: 1500,
          mask: false,
        });
      }
      this.setData({
        listTrain:list,
        page:{
          currPage,
          totalPage
        }
      })
     } catch (error) {
      wx.showLoading({
        title: '这一页没有数据',
        mask: true,
      });
     }
    
  },

  handlechangecurr(e){
    const {curr} = e.detail;
    this.listTrain(curr)
  },
  onLoad: function (options) {
    this.listTrain(1)
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