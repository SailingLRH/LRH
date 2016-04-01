$(function(){
	var videoId='f0018ozndt1&duration=6691&cid=c17yx4mna94c9rw&tpid=1&showend=1&showcfg=1&searchbar=1&shownext=1&list=2&autoplay=1&ptag=www_hao123_com%7C%7Bpagetype%7D.movie.top1&outhost=http%3A%2F%2Fv.qq.com%2Fcover%2Fc%2Fc17yx4mna94c9rw.html&refer=http%3A%2F%2Fv.qq.com%2F&openbc=1&fakefull=1&bullet=1&bulletid=&title=%E6%8D%89%E5%A6%96%E8%AE%B0';
	var video = new tvp.VideoInfo();
	//向视频对象传入视频vid
	video.setVid(videoId);
	
	var width=$("#mod_player").parent().width();
	var height=width/16*9;
	var player = new tvp.Player(width, height);
	player.setCurVideo(video);
	player.addParam("autoplay",1);//0表示不自动播放,1表示自动播放
	player.addParam("wmode","transparent");//设置透明化，不设置时，视频为最高级，总是处于页面的最上面，此时设置z-index无效
	player.addParam("flashskin","http://imgcache.qq.com/minivideo_v1/vd/res/skins/TencentPlayerMiniSkin.swf");//此参数使视频窗口为小窗口。没有此参数，默认为大窗口
	player.addParam("showend",1);//结束界面是视频本身的界面
	player.addParam("adplay",0);//播放广告
	player.addParam("showcfg",1);//显示控件
	player.addParam("player",'auto');//播放器类型
	//输出播放器    
	player.write("mod_player");
});
