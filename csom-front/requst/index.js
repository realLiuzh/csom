let ajaxTimes = 0;
export const request=(params)=>{
  //判断是否又token 没有就带上
   let header = {...params.header};
  //  if(params.url.includes("/like/")){
  //    //拼接上header 带上token
  //    header["Content-Type"] = 'multipart/form-data'
  //  }

  ajaxTimes++;
  //显示加载中效果
   wx.showLoading({
     title: "加载中",
     mask: true,
   });
  //定义公共的url
  const  baseUrl="https://www.muxiushangmen.ltd/csom"
  return new Promise((resolve,reject)=>{
    wx.request({
      ...params,//对传进来的值进行解构
      header:header,
      url:baseUrl+params.url,
      success:(result)=>{
        resolve(result);
      },
      fail:(err)=>{
        reject(err);
      },
      complete:()=>{
        ajaxTimes--;
        //关闭图标
        if(ajaxTimes === 0){
          wx.hideLoading();
        }
      }
    })
  })
}