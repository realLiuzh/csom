import {request} from"../../requst/index.js";
import regeneratorRuntime from"../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    result:[],
    isFocus:false,
    inputValue:""
  },
  timeId:-1,
  handleInput(e){
      //获取输入框的值
      const {value} = e.detail;
      //检测合法性
      if(!value.trim()){
        //值不合法
        this.setData({
          result:[],
          isFocus:false
        })
        return;
      }
      this.setData({
        isFocus:true
      })
      //准备发送请求
       clearTimeout(this.timeId);
       this.timeId = setTimeout(() =>{
         this.qsearch(value);
       },500)
      
  },
  async qsearch(query){
    const res = await request({url:'/article/search',data:{key:query}});
    console.log(res);
    let {result} = res.data
     this.setData({
      result
     })
     if(result.length == 0){
      wx.showToast({
        title: '未找到该文章',
        icon: 'error',
        duration: 1500,
        mask: false,
      });
    }
  },
  handleCancel(){
    this.setData({
      inputValue:"",
      isFocus:false,
      result:[]
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function () {

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