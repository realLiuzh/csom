import {request} from"../../requst/index.js";
import regeneratorRuntime from"../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    //驾驶员的列表
    list:[],
    //分页表
    tabs:[
      {
        id:0,
        name:"驾驶员日常维护",
        isActive:true,
      },{
        id:1,
        name:"修理工日常维修",
        isActive:false,
      }
    ],
    //维修工日常
    listdrive:[
      {
        id:0,
        name:"技术培训",
        url:'/pages/listdrive/listdrive?id=0' ,
      },{
        id:1,
        name:"维修案例分析",
        url:'/pages/analysis/analysis?id=1',
      },{
        id:2,
        name:"软件下载",
        url:'/pages/downword/downword?id=2',
      }
    ],
    //页码
    page:{
      currPage:'1',//当前页
      totalPage:'',//总页数
    },
    showpage:true,//维修工是隐藏
    result:'',//点赞数
  },
  //总浏览数
  async pagetotle(){
    const res = await request({url:'/article/total/visit'});

    const {result} = res.data
    this.setData({
      result
    })
  },

  handleItemChange(e){
    const {index} = e.detail;
     //获取data数组
      let {tabs} = this.data;
      //循环数组
      tabs.forEach((V,i) =>i === index?V.isActive=true:V.isActive=false);
      this.setData({
        tabs
      })
      if(tabs[1].isActive === true){
        this.setData({
          showpage:false
        })
      }else{
        this.setData({
          showpage:true
        })
      }
  },
  //请求驾驶员数据
  
  async listdrive(curr){
    try {
      const res = await request({url:'/article/list',method:"GET",data:{page:curr,type:0}});

      let {list} = res.data.result
      let {currPage} =res.data.result
      let {totalPage} =res.data.result
      this.setData({
        list,
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
  //获取openid
  async putCode(code){
    const res = await request({url:'/article/user/like',data:{likedPoseId:code}});
    console.log(res)
    const {openId} = res.data.result
    //建立openid
    wx.setStorage({
      key:'openId',
      data:openId
    })
  },
  //获取点击的页面列表
  handlechangecurr(e){
    const {curr} = e.detail;
    this.listdrive(curr)
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    const curren = 1
    this.listdrive(curren)
    wx.login({
      success: (result) => {
        this.putCode(result.code)
      },
    });
    this.pagetotle()
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