// components/currentpage/currentpage.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    page:{
      type:Symbol,
      value:{}
    },
  },

  /**
   * 组件的初始数据
   */
  data: {
    currPage:'',
    totalPage:''
  },

  /**
   * 组件的方法列表
   */
  methods: {
    //上一页
    handfirst(e){
     if(this.data.page.currPage != 1){
       const {curr} = e.currentTarget.dataset;
      this.triggerEvent("itemcurrPage",{curr});
     }else{
       wx.showToast({
         title: '已经是第一页了',
         icon: 'none',
         duration: 1500,
       });
     }
    },
    //下一页
    handlast(e){
       if(this.data.page.totalPage != this.data.page.currPage){
        const {curr} = e.currentTarget.dataset;
        this.triggerEvent("itemcurrPage",{curr});
       }else{
         wx.showToast({
           title: '已经是最后一页了',
           icon: 'none',
           duration: 1500,
         });
       }
    },
    handleitem(e){
      console.log(e)
      const {curr} = e.currentTarget.dataset;
      this.triggerEvent("itemcurrPage",{curr});
    }
  },
})
