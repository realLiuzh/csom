import {request} from"../../requst/index.js";
import regeneratorRuntime from"../../lib/runtime/runtime";
Page({

  /**
   * 页面的初始数据
   */
  data: {
    content:'',//文章内容
    title:'',//标题
    author:'',//作者
    good:true,//点赞
    articleId:'',
    openId:'',
    goodlist:'',//已经点赞的列表
    visitNum:'',//浏览量
    likeNum:'',//点赞量
    signature:{
      edit:'',
      check:'',
      finalCheck:'',
      author:''  
    },
    urls:[],//图片数组
    current:'',
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
    const articleId = options.id
    this.getArticle(articleId)
    const openId = wx.getStorageSync('openId')
    this.setData({
      articleId,
      openId
    })  
    this.findgood()   
    this.pageview()
    // wx.previewImage({
    //   current: 'http://47.96.86.132:8080/csom/file/download?name=图片1.png', // 当前显示图片的http链接
    //   urls: ['http://47.96.86.132:8080/csom/file/download?name=图片1.png',
    //           'http://47.96.86.132:8080/csom/file/download?name=图片2.png',] // 需要预览的图片http链接列表
    // })
  },
  //放大图片
  bigImg(){
    wx.previewImage({
      current: this.data.current, // 当前显示图片的http链接
      urls: this.data.urls// 需要预览的图片http链接列表
    })
  },
  
  
  //更改图标
  changeImgs(){
    let good = (this.data.good === true?false:true);
    this.setData({
      good
    })
    if(this.data.good == false){
        wx.showToast({
        title: '已点赞',
        duration: 1000,
        mask: false,
      });
      let likeNum = this.data.likeNum+1
      this.setData({
        likeNum
      })
    }else{
      let likeNum = this.data.likeNum-1
      this.setData({
        likeNum
      })
    }
    this.putgood()
  },
  //点赞后台记录
  async putgood(){
    await request({url:'/article/like',method:"POST",data:{"articleId":this.data.articleId,"userId":this.data.openId}});
  },
  //获取文章数据

   async getArticle(id){
     const res = await request({url:'/article/details',data:{id}});
     console.log(res)
     let {content} = res.data.result
     const {title} = res.data.result
     const {author} = res.data.result
     const {edit} =res.data.result
     const {check} =res.data.result
     const {finalCheck} =res.data.result
     const {urls} =res.data.result
     const current =res.data.result.urls[0]
     content = content.replace(/\<img/gi, '<img style="max-width:100%;height:auto"')//将图片铺满
     this.setData({
       content,
       title,
       author,
       urls,
       current,
       signature:{
         edit,
         check,
         finalCheck,
         author
       },
     })
   },

  //浏览量加点赞量
  async pageview(){
    console.log(this.data.articleId)
    const res = await request({url:'/article/total/'+this.data.articleId});

    const {likeNum} =res.data.result
    const {visitNum} =res.data.result
    this.setData({
      likeNum,
      visitNum
    })
  },
  //检测是否点赞
  async findgood(){
    const res = await request({url:'/article/islike',data:{userId:this.data.openId,articleId:this.data.articleId}});
    let isgood = res.data.result

    if(isgood == false){

      this.setData({
        good:true
      })
    }else{
      console.log('123')
      this.setData({
        
        good:false
      })
    }
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