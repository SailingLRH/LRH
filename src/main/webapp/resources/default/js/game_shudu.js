var isStart=false;//全局变量,用于判断游戏是否已启动
var isOver=false;//全局变量,如果游戏结束,那么快捷键将不可用
var select_cell;//全局变量,选中的单元格
var time=0;//全局变量，游戏时间
var date=null;//全局变量，用于记录开始时间,单位：秒
var time_info="00:00:00";//全局变量,方便在不同函数共享数据
var hasConfirm=false;//全局变量.是否confirm弹窗,如果有,就不能弹普通窗
var alertTimeout=null;//全局变量,延时弹窗,必要的时候要清除
var gameAll=null;//全局变量,用于临时保存从后台加载的游戏镜像;
var time_temp=null;//全局变量,用于临时保存从后台加载的游戏时间;
var setTimeInfo_timeOut=null;//全局变量,用于递归调用显示游戏时间的函数
var hasTask=false;//是否已经调用过了定时器

$(function(){
	//加载页面的时候就自动去检测是否保存过该游戏的进度
	if(typeof(gameType) != "undefined" && typeof(gameName) != "undefined"){
		$.post("/game/load",{
			gameType:gameType,
			gameName:gameName
		},function(result){
			if(result.isSuccess){
				//setCookie("gameAll",result.data); 没用的cookie!
				gameAll=result.data.gameAll;
				time_temp=result.data.gameTime;
				confirmDiv("检测到您在 <B style='color:green;'>"+result.data.addTime+"</B> 有保存过该游戏进度,要继续上次的进度吗?","loadGame");
			}
		});
	}
	
	//智能根据浏览器显示，如果浏览器宽度较小，则隐藏游戏规则，更小则隐藏操作模块
	hideOrShowRuleAndBtn();
	
	//点击单元格则显示浮悬候选数字div
	$(".game_win").click(function(e){
		if($(e.target).is(".court .normal .number_p")){
			if(isStart){//控制开关功能
				//获取被点击的单元格
				var cell_number=$(e.target);
				select_cell=$(e.target);//赋值给全局变量,方便清除此单元格的数据
				var cell=cell_number.parent();
				var className=cell.attr('class');
				//以空格将class属性分开(要求页面上的class属性书写规范)
				var classNameArray=className.split(" ");
				
				//判断点击的单元格是否已经点击过了，如果是收起备选数字
				var is_click_cell=false;
				for(var i=0;i<classNameArray.length;i++){
					if(classNameArray[i].indexOf("click_cell")!=-1){
						is_click_cell=true;
						break;
					}
				}
				if(is_click_cell){
					//清除已有的浮悬div
					$(".answer_div").remove();
					$(".answer_div2").remove();
					//移除特殊class属性
					$(".court .normal").removeClass("click_cell");
					//所有普通单元格背景色/前景色初始化(除了警告过的)
					$(".court .normal:not(.warn_cell) .number_p").stop().animate({backgroundColor:"#f2f2f2",color:"#9900ff"},1000);
					select_cell=null;
					return false;//到此为止，不执行以下代码
				}
				
				$("div.cell").css('z-index','500');
				//清除已有的浮悬div
				$(".answer_div").remove();
				$(".answer_div2").remove();
				//移除特殊class属性
				$(".court .normal").removeClass("click_cell");
				//所有普通单元格背景色/前景色初始化(除了警告过的)
				$(".court .normal:not(.warn_cell) .number_p").stop().animate({backgroundColor:"#f2f2f2",color:"#9900ff"},1000);
				
				//为了更方便地找到这个单元格,为其添加特殊class属性
				cell.addClass("click_cell");
				//判断该单元格是否有被警告
				var is_warn=false;
				//遍历数组找出定位class属性
				for(var i=0;i<classNameArray.length;i++){
					if(classNameArray[i].indexOf("warn_cell")!=-1){
						is_warn=true;
						break;
					}
				}
				if(is_warn){
					cell_number.stop().css({"background-color":"#ff0000","color":"#ffff00"});
				}else{
					cell_number.stop().css({"background-color":"#999999","color":"cyan"});
				}
				
				//为被点击单元格添加备选数字div
				var div='';
				if(className.indexOf("row7")!=-1 || className.indexOf("row8")!=-1 || className.indexOf("row9")!=-1){
					div='<div class="answer_div2 needShadow">';
				}else{
					div='<div class="answer_div needShadow">';
				}
				cell.css('z-index','9999').append(div+
					'<div class="jiantou">&nbsp;</div>'+
					'<p class="cell son_left_top" onclick="setAnwser(1)">1</p>'+
					'<p class="cell son_middle_top" onclick="setAnwser(2)">2</p>'+
					'<p class="cell son_right_top" onclick="setAnwser(3)">3</p>'+
					'<div class="clear"></div>'+
					'<p class="cell son_left_middle" onclick="setAnwser(4)">4</p>'+
					'<p class="cell son_middle_middle" onclick="setAnwser(5)">5</p>'+
					'<p class="cell son_right_middle" onclick="setAnwser(6)">6</p>'+
					'<div class="clear"></div>'+
					'<p class="cell son_left_bottom" onclick="setAnwser(7)">7</p>'+
					'<p class="cell son_middle_bottom" onclick="setAnwser(8)">8</p>'+
					'<p class="cell son_right_bottom" onclick="setAnwser(9)">9</p>'+
					'<div class="clear"></div>'+
				'</div>');
			}else{
				if(isOver){
					alertDiv("游戏已经结束,界面不可操作!");
				}else{
					alertDiv("开始游戏之后才能操作!");
				}
			}
		}
	});
	
});

//当备选数字被点击时----------------->整个游戏的核心函数
function setAnwser(num){
	$(".click_cell .number_p").html(num);
	
	//加载游戏进度
	changeProcess();
	
	//清除浮悬div
	$(".answer_div").remove();
	$(".answer_div2").remove();
//------------难点:判断所填入的数字在所属行,所属列,所属宫是否有重复-------------------------	
	//获取所属宫的所有已填数字
	var numbers=$(".click_cell").parent().children(".cell").children(".number_p");
	var x=0;
	for(var i=0;i<9;i++){//将所填数字与所属宫里的数字进行比较(包括和自己比较,所以会有一次重复)
		var num1=numbers[i].innerHTML
		if(num1!=null && num1 !='' && num==num1){
			x++;
		}
	}
	if(x>1){//表示所填入的数字和宫中已有的数字有重复
		alertDiv("所填入的数字和所属<B>[宫]</B>中已有的数字有重复!");
		//设置成警告
		$(".click_cell").addClass("warn_cell");
		$(".click_cell .number_p").css({"background-color":"#ff0000","color":"#ffff00"});
		//移除特殊class属性
		$(".click_cell").removeClass("click_cell");
		select_cell=null;//清除全局变量数据
		return false;//终止
	}
	
//--------------------判断所属行/列是否有数字和所填数字重复--------------------------------
	var className=$(".click_cell").attr('class');
	//以空格将class属性分开(要求页面上的class属性书写规范)
	var classNameArray=className.split(" ");
	var column="";
	var row="";
	var djx1="";//对角线1
	var djx2="";//对角线2
	//遍历数组找出定位class属性
	for(var i=0;i<classNameArray.length;i++){
		if(classNameArray[i].indexOf("column")!=-1) column=classNameArray[i];
		if(classNameArray[i].indexOf("row")!=-1) row=classNameArray[i];
		if(classNameArray[i].indexOf("djx1")!=-1) djx1="djx1";
		if(classNameArray[i].indexOf("djx2")!=-1) djx2="djx2";
	}
	//console.log(row+"\t"+column);
	if(column=="" || row==""){
		alertDiv("抱歉,系统出错了!<br/>不能为你校验所填入的数字在所属行/列是否有重复!");
		return false;
	}
	//判断行中数字是否与填入数字有重复
	var rowNum=$("."+row+" .number_p");
	for(var i=0,x=0;i<9;i++){//将所填数字与所属行里的数字进行比较(包括和自己比较,所以会有一次重复)
		var num1=rowNum[i].innerHTML
		if(num1!=null && num1 !='' && num==num1){
			x++;
		}
	}
	if(x>1){//表示所填入的数字和所属行中已有的数字有重复
		alertDiv("所填入的数字和所属<B>[行]</B>中已有的数字有重复!");
		//设置成警告
		$(".click_cell").addClass("warn_cell");
		$(".click_cell .number_p").css({"background-color":"#ff0000","color":"#ffff00"});
		//移除特殊class属性
		$(".click_cell").removeClass("click_cell");
		select_cell=null;//清除全局变量数据
		return false;//终止
	}
	
	//判断列中数字是否与填入数字有重复
	var columnNum=$("."+column+" .number_p");
	for(var i=0,x=0;i<9;i++){//将所填数字与所属列里的数字进行比较(包括和自己比较,所以会有一次重复)
		var num1=columnNum[i].innerHTML
		if(num1!=null && num1 !='' && num==num1){
			x++;
		}
	}
	if(x>1){//表示所填入的数字和所属列中已有的数字有重复
		alertDiv("所填入的数字和所属<B>[列]</B>中已有的数字有重复!");
		//设置成警告
		$(".click_cell").addClass("warn_cell");
		$(".click_cell .number_p").css({"background-color":"#ff0000","color":"#ffff00"});
		//移除特殊class属性
		$(".click_cell").removeClass("click_cell");
		select_cell=null;//清除全局变量数据
		return false;//终止
	}
	//------------------判断对角线---------------
	if(djx1!=""){
		var djx=$(".djx1 .number_p");
		for(var i=0,x=0;i<9;i++){//将所填数字与所属行里的数字进行比较(包括和自己比较,所以会有一次重复)
			var num1=djx[i].innerHTML
			if(num1!=null && num1 !='' && num==num1){
				x++;
			}
		}
		if(x>1){//表示所填入的数字和所属行中已有的数字有重复
			alertDiv("所填入的数字和所属<B>[对角线1]</B>中已有的数字有重复!");
			//设置成警告
			$(".click_cell").addClass("warn_cell");
			$(".click_cell .number_p").css({"background-color":"#ff0000","color":"#ffff00"});
			//移除特殊class属性
			$(".click_cell").removeClass("click_cell");
			select_cell=null;//清除全局变量数据
			return false;//终止
		}
	}
	if(djx2!=""){
		var djx=$(".djx2 .number_p");
		for(var i=0,x=0;i<9;i++){//将所填数字与所属行里的数字进行比较(包括和自己比较,所以会有一次重复)
			var num1=djx[i].innerHTML
			if(num1!=null && num1 !='' && num==num1){
				x++;
			}
		}
		if(x>1){//表示所填入的数字和所属行中已有的数字有重复
			alertDiv("所填入的数字和所属<B>[对角线2]</B>中已有的数字有重复!");
			//设置成警告
			$(".click_cell").addClass("warn_cell");
			$(".click_cell .number_p").css({"background-color":"#ff0000","color":"#ffff00"});
			//移除特殊class属性
			$(".click_cell").removeClass("click_cell");
			select_cell=null;//清除全局变量数据
			return false;//终止
		}
	}
	
//--------------无重复则执行以下代码----------------------------------	
	//下面这行代码的意义在于，开始填入的数字有误，后面该对了，将警告色去除
	$(".click_cell .number_p").css({"background-color":"#999999","color":"cyan"});
	
	select_cell=null;//清除全局变量数据
	//检查无重复后,将警告消除
	$(".click_cell").removeClass("warn_cell");
	//移除特殊class属性
	$(".click_cell").removeClass("click_cell");
	//2秒钟后,背景色和前景色初始化
	window.setTimeout("initNormalCell()", 2000);
//------------判断数独中的数字是否已经全部填写完了-------------------------
	var f=true;
	for(var i=1;i<10;i++){
		var rowNum1=$(".row"+i+" .number_p");
		for(var j=0;j<rowNum1.length;j++){
			if(rowNum1[j].innerHTML==null || rowNum1[j].innerHTML==""){
				f=false;
				break;
			}
		}
		if(!f) break;
	}
	
	if(f){//表示所有的数字都填写完了,接下来进行最终的校验
		isComplete();
	}
}

//渐变初始化普通单元格
function initNormalCell(){
	//所有普通单元格背景色/前景色初始化(除了警告过的)
	$(".court .normal:not(.warn_cell) .number_p").stop().animate({backgroundColor:"#f2f2f2",color:"#9900ff"},3000);
}
//---------------------终极判断,游戏是否已结束---------->又一个核心函数
function isComplete(){
	//alertDiv("正在校验结果...");
	var f=true;
	for(var i=1;i<10;i++){//1.每一行都要出现1~9
		var row=$(".row"+i+" .number_p");
		var rowNum="";
		for(var j=0;j<row.length;j++){
			rowNum+=row[j].innerHTML;
		}
		for(k=1;k<10;k++){
			if(rowNum.indexOf(k)==-1){//表示这一行中,1~9至少有一个数字没出现,不符合游戏规则,校验终止
				f=false;
				break;
			}
		}
		if(!f){
			alertDiv("第<B>["+i+"]</B>行不满足‘每行都要出现1~9’的游戏规则,游戏未完成!");
			break;
		}
	}
	if(!f) return false;//如果上面的校验都没有通过,那下面的校验就没必要进行
	
	for(var i=1;i<10;i++){//1.每一列都要出现1~9
		var column=$(".column"+i+" .number_p");
		var columnNum="";
		for(var j=0;j<column.length;j++){
			columnNum+=column[j].innerHTML;
		}
		for(k=1;k<10;k++){
			if(columnNum.indexOf(k)==-1){//表示这一列中,1~9至少有一个数字没出现,不符合游戏规则,校验终止
				f=false;
				break;
			}
		}
		if(!f){
			alertDiv("第<B>["+i+"]</B>列不满足‘每列都要出现1~9’的游戏规则,游戏未完成!");
			break;
		}
	}
	if(!f) return false;//如果上面的校验都没有通过,那下面的校验就没必要进行
	
	var courts=$(".court");//获取所有的宫
	for(var i=0;i<courts.length;i++){//每个宫都要出现1~9
		var court_cell=$(courts[i]).children(".cell").children(".number_p");
		var courtNum="";
		for(var j=0;j<court_cell.length;j++){
			courtNum+=court_cell[j].innerHTML;
		}
		for(k=1;k<10;k++){
			if(courtNum.indexOf(k)==-1){//表示这个宫中,1~9至少有一个数字没出现,不符合游戏规则,校验终止
				f=false;
				break;
			}
		}
		if(!f){
			switch (i) {
			case 0:
				alertDiv("<B>[1,1]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
				
			case 1:
				alertDiv("<B>[1,2]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 2:
				alertDiv("<B>[1,3]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 3:
				alertDiv("<B>[2,1]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 4:
				alertDiv("<B>[2,2]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 5:
				alertDiv("<B>[2,3]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 6:
				alertDiv("<B>[3,1]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 7:
				alertDiv("<B>[3,2]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			case 8:
				alertDiv("<B>[3,3]</B>宫不满足‘每个宫都要出现1~9’的游戏规则,游戏未完成!");
				break;
			}
			break;
		}
	}
	if(f){//所有的检验都通过了,弹出庆祝窗
		//alertDiv("您填对了所有的数字,好棒呀!");//暂时先用这个
		successful();
	}
}
//-------------------------游戏按钮--------------------
//开始游戏
function play(){
	isStart=true;
	alertDiv("游戏开始...");
	$(".game_status").html("游戏中...");
	//将开始按钮变成暂停按钮
	var html_str='<a href="javaScript:void(0)" onclick="stop()" title="暂停游戏" class="stop stop_able"></a>';
	$(".play_or_stop").html(html_str);
	//开始计时
	date=parseInt((new Date().getTime())/1000);
	setTimeInfo();
	
	//开启一个定时器,每隔25分钟刷新一次存在,确保游戏中,不会因session失效而掉线 25*60*1000
	task(25*60*1000);
	hasTask=true;
	
}
//将时间信息显示到页面上
function setTimeInfo(){
	var date1=new Date();
	time=parseInt((date1.getTime())/1000-date);//单位：秒
	
	if(time>59){
		var min=parseInt(time/60);
		var sec=time%60;
		if(min>59){
			var hou=parseInt(min/60);
			min=min%60;
			var hou1=addZero(hou);
			var min1=addZero(min);
			var sec1=addZero(sec);
			time_info=hou1+":"+min1+":"+sec1;
		}else{
			var min1=addZero(min);
			var sec1=addZero(sec);
			time_info="00:"+min1+":"+sec1;
		}
	}else{ 
		var time1=addZero(time);
		time_info="00:00:"+time1;
	}
	
	$(".game_time").html(time_info);
	if(isStart){
		//递归，不断更新时间
		setTimeInfo_timeOut=window.setTimeout("setTimeInfo()", 1000);
	}
}

//清除指定单元格数字  && window.confirm("您确定要清除该单元格中的数字吗？")
function clearCell(){
	if(isOver){
		alertDiv("游戏已经结束,此按钮不可用！");
	}else{
		if(select_cell!=null){
			select_cell.html("");
			//加载游戏进度
			changeProcess();
			//清除浮悬div
			$(".answer_div").remove();
			$(".answer_div2").remove();
			//检查无重复后,将警告消除
			select_cell.parent().removeClass("warn_cell");
			//移除特殊class属性
			select_cell.parent().removeClass("click_cell");
			//背景色和前景色初始化
			initNormalCell();
			select_cell=null;//清除全局变量数据
		}else{
			alertDiv("请先选中要清除数字的单元格！");
		}
	}
}

//暂停游戏
function stop(){
	//清除浮悬div
	$(".answer_div").remove();
	$(".answer_div2").remove();
	if(select_cell!=null){
		//移除特殊class属性
		select_cell.parent().removeClass("click_cell");
		select_cell=null;//清除全局变量数据
	}
	//背景色和前景色初始化
	initNormalCell();
	
	isStart=false;
	var html_str="";
	if(isOver){
		//alertDiv("游戏完成！");
		$('body,html').animate({scrollTop:0},500);
		$(".game_status").html("已完成");
		//将暂停按钮变成停止按钮
		html_str='<a href="javaScript:void(0)" title="游戏结束,点击返回游戏首页" class="complete" onclick="goBackIndex(1)"></a>';
		$(".play_or_stop").html(html_str);
	}else{
		alertDiv("游戏已暂停！");
		$(".game_status").html("已暂停");
		//将暂停按钮变成开始按钮
		html_str='<a href="javaScript:void(0)" onclick="go_on()" title="继续游戏" class="play play_able"></a>';
		$(".play_or_stop").html(html_str);
	}
	
	if(!hasTask){
		//开启一个定时器,没25分钟刷新一次存在,确保游戏中,不会session失效而掉线
		task(25*60*1000);
		hasTask=true;
	}			
}
//继续游戏
function go_on(){
	alertDiv("游戏继续...");
	//将开始按钮变成暂停按钮
	var html_str='<a href="javaScript:void(0)" onclick="stop()" title="暂停游戏" class="stop stop_able" ></a>';
	$(".play_or_stop").html(html_str);
	//继续计时
	date=parseInt((new Date().getTime())/1000);
	date-=time;
	$(".game_status").html("游戏中...");
	isStart=true;
	setTimeInfo();
}

//刷新页面重新开始
function replay(f){
	if(f!=null && f==1) 
		window.location.href=window.location.href;
	else 
		confirmDiv("此操作将会清空所有已经填好的数字，并且不能恢复！</br>您确定要重新开始吗？","replay");
}

//每填入一个数字或清除一个数字就重新计算游戏进度
function changeProcess(){
	var all=$(".normal .number_p");
	var all_num=all.length;
	var done=0;
	for(var i=0;i<all_num;i++){
		num=all[i].innerHTML;
		if(num!=null && num !='') done++;
	}
	var process=100*done/all_num;
	process=process.toFixed(2);
	if(process==0){
		$(".game_process").html("0.00%");
	}else{
		$(".game_process").html(process+"%");
	}
//	$(".process_bar p").stop().animate({"width":process+"%"},"fast");
}

//游戏通过
function successful(){
	if(typeof(LRH) != "undefined" && LRH){
		$(".LRH:not(.LRH_images)").show();
	}
	hideOrShowRuleAndBtn();//初始化遮罩层和LRH元素位置
	//屏蔽快捷键
	isOver=true;
	stop();//调用上面的stop方法停止计时
	//遮罩层滑下来
	$(".zz_div").stop().animate({"top":"0px"},"fast","swing",function(){
		hasConfirm=true;
		//生成过关信息
		$("body").append('<div class="successful_info_div">'
				+'<p class="close_btn"><a href="javaScript:void(0)" onclick="closeSuccessfulDiv()"></a></p>'
				+'<H1>恭喜过关!</H1>'
				+'<p>'
				+'<label>游戏用时:</label>'
				+'<span>'+time_info+'</span>'
				+'</p>'
				+'<div class="successful_btn">'
				+'<p class="toIndex"><a href="/game">返回首页</a></p>'
				+'<p class="onceAgin"><a href="javaScript:void(0)" onclick="replay(1)">再玩一遍</a></p>'
				+'<div class="clear"></div>'
				+'</div>'
				+'</div>');
	});
	
	if(typeof(LRH) != "undefined" && LRH){
		//1秒后执行LRH数独特殊动画
		LRH_timeOut=window.setTimeout("LRH_animation()", 1000);
	}
}
//LRH数独独有动画
function LRH_animation(){
	window.clearTimeout(LRH_timeOut);
	$(".successful_info_div").stop().animate({"top":"500px"},2000,"swing",function(){
		$("p.H7").animate({top:"330px",backgroundColor:"#00ccff"},3500,"swing",function(){
			$("p.H6").animate({top:"300px",backgroundColor:"#00ccff"},3200,"swing",function(){
				$("p.H5").animate({top:"270px",backgroundColor:"#00ccff"},2900,"swing",function(){
					$("p.H4").animate({top:"240px",backgroundColor:"#00ccff"},2600,"swing",function(){
						$("p.H3").animate({top:"210px",backgroundColor:"#00ccff"},2300,"swing",function(){
							$("p.H2").animate({top:"180px",backgroundColor:"#00ccff"},2000,"swing",function(){
								$("p.H1").animate({top:"150px",backgroundColor:"#00ccff"},1700,"swing",function(){
									$(".cube").stop(true);
									$("p.L3").animate({"left":"+=10px"},500);
									$("p.L5").animate({"left":"-=10px"},500);
									
									$("p.L2").animate({"left":"+=20px"},500);
									$("p.L6").animate({"left":"-=20px"},500);
									
									$("p.L1").animate({"left":"+=30px"},500);
									$("p.L7").animate({"left":"-=30px"},500);
									
									$("p.H3").animate({"top":"+=10px"},500);
									$("p.H5").animate({"top":"-=10px"},500);
									
									$("p.H2").animate({"top":"+=20px"},500);
									$("p.H6").animate({"top":"-=20px"},500);
									
									$("p.H1").animate({"top":"+=30px"},500);
									$("p.H7").animate({"top":"-=30px"},500,"swing",function(){
										//很高兴你会看我写的js源码.告诉你一个有趣的事,将上面第547行代码注释掉,并将下面的第569行代码注释
										//用浏览器查看效果,你会发现,那些小方块像有了生命一样,四处乱走,根本停不下来!不信?试一下吧!
										
										//1秒后执行LRH数独特殊动画-下半场
										LRH_timeOut2=window.setTimeout("LRH_animation_continue()", 1000);
									});
								});
							});
						});
					});
				});
			});
		});
	});
}
//LRH动画下半场
function LRH_animation_continue(){
	window.clearTimeout(LRH_timeOut2);
	$(".cube").stop(true);
	//用图片取代小方块
	$(".LRH_images").show();
	$(".cube").hide();
	
	$("p.image_L").animate({left:"-=130px"},3000,"swing",function(){
		$("p.image_L").stop(true);
		$("p.image_R").animate({left:"+=70px"},1500,"swing",function(){
			
			$("p.image_H").animate({top:"-=80px"},2000,"swing");
			$("p.image_R").stop().animate({top:"+=80px"},2000,"swing",function(){
				$("p.image_R").stop().animate({left:"-=130px"},3000,"swing");
				$("p.image_H").stop().animate({left:"+=130px"},3000,"swing",function(){
					$("p.image_R").stop().animate({top:"-=80px"},2000,"swing");
					$("p.image_H").stop().animate({top:"+=80px"},2000,"swing",function(){
						$(".successful_info_div").stop().animate({"top":"200px"},10000,"swing");
						$(".image_L,.image_R,.image_H").stop().animate({top:"-140px"},15000,"swing",function(){
							$(".image_L,.image_R,.image_H").hide();
						});
					});
				});
			});
		});
	});
}

//返回首页
function goBackIndex(f){
	if(f!=null && f==1){
		window.location.href="/game";
	}else{
		confirmDiv("确定要放弃本局游戏回到首页吗?","goBackIndex");
	}
}
//----------------------------公共----------------------------------
//智能根据浏览器宽智能调整元素大小
function hideOrShowRuleAndBtn(){
	//获取浏览器高
	var w=$(window).width();
	var h=$(window).height();
	//获取页面高
	var html_h=$("html").height();
	
	var win_w=$(".win").width();
	if(win_w>=670){
		$(".game_process_lable").html("游戏进度:");
		$(".game_time_lable").html("游戏时间:");
		$(".game_status_lable").html("游戏状态:");
		$(".game_level_lable").html("游戏难度:");
	}else{
		$(".game_info .lable").html("");
	}
	
//	//-------------设置遮罩层宽高和定位
	var html_w=$(document).width();
	h=h>html_h?h:html_h;
	
	w=w>=378?w:html_w;
	//-------------LRH数独独有的---------
	if(typeof(LRH) != "undefined" && LRH){
		//cube元素定位(水平居中)
		var margin_left=(w-140)/2;
		$(".image_L").css("left",margin_left+"px");
		$(".image_R").css("left",(margin_left+60)+"px");
		$(".image_H").css("left",margin_left+"px");
		if($(".cube").css("top")=='-21px'){
			margin_left=(w-200)/2;
			$("p.L1").css("left",margin_left+"px");
			$("p.L2").css("left",(margin_left+30)+"px");
			$("p.L3").css("left",(margin_left+60)+"px");
			$("p.L4").css("left",(margin_left+90)+"px");
			$("p.L5").css("left",(margin_left+120)+"px");
			$("p.L6").css("left",(margin_left+150)+"px");
			$("p.L7").css("left",(margin_left+180)+"px");
		}else{
			$("p.L1").css("left",margin_left+"px");
			$("p.L2").css("left",(margin_left+20)+"px");
			$("p.L3").css("left",(margin_left+40)+"px");
			$("p.L4").css("left",(margin_left+60)+"px");
			$("p.L5").css("left",(margin_left+80)+"px");
			$("p.L6").css("left",(margin_left+100)+"px");
			$("p.L7").css("left",(margin_left+120)+"px");
		}
	}
}

//游戏首页定位到指定游戏位置
function findGame(index){
	switch(index){
		case 1:
			$(".shudu_level").show();
			var h=$(".shudu_level").offset().top;
			$('body,html').animate({scrollTop:h+"px"},1000);
//			var winBody=$(".shudu_level .win_body_all");
//			var min_h=winBody.height()+parseInt(winBody.css("marginTop"))+parseInt(winBody.css("marginBottom"));
			$(".shudu_level .win_body").stop().slideDown(1000);
			$(".shudu_level .win_controll_max_btn a").removeClass("able").addClass("unable");
			$(".shudu_level .win_controll_min_btn a").removeClass("unable").addClass("able");
			break;
	}
}

//关闭过关信息窗
function closeSuccessfulDiv(){
	if(typeof(LRH) != "undefined" && LRH){
		$(".LRH").stop(true).hide();
		//初始化LRH元素位置信息
		$(".cube").css("top","-21px");
		$(".word").css("left","-719px");
		$(".LRH_images").css("top","180px");
	}
	//移除过关信息
	$(".successful_info_div").remove();
	hideZZ();
	hasConfirm=false;
}

//点击更多时
function showMore(){
	alertDiv("啊哈~真伤脑筋,居然没有了!也许将来会有的,催催开发者吧! *^_^*");
}

function saveGame(){
	alertDiv("正在保存游戏进度,请稍后...");	
//	var gameAll=$(".game_win").html().replace(/[\r\n\t ]/g,'');
	var gameAll=$(".game_win").html().replace(/[\r\n\t]/g,'');
	console.log("替换后的长度:"+gameAll.length);
	$.post("/game/save",{
		gameType:gameType,
		gameName:gameName,
		gameTime:time,
		gameAll:gameAll
	},function(result){
		alertDiv(result.msg);
	});
}
//加载游戏进度
function loadGame(f){
	if(f=='1'){
		slideUpNoticeDiv(1);
		//var gameAll=getCookie("gameAll");
		//delCookie("gameAll");
		
		//清除定时刷新时间
		window.clearTimeout(setTimeInfo_timeOut);
		
		isStart=false;
		//先调用stop方法暂停游戏
		stop();
		$(".game_win").html(gameAll);
		time=time_temp;
		//再调一次stop方法,以防按钮显示为已开始
		stop();
		//调用调整大小的方法
		hideOrShowRuleAndBtn();
		time_temp=null;
		gameAll=null;
	}else{
		alertDiv("正在检测您保存的游戏进度...");
		$.post("/game/load",{
			gameType:gameType,
			gameName:gameName
		},function(result){
			alertDiv(result.msg);
			if(result.isSuccess){
				//setCookie("gameAll",result.data); 没用的cookie!
				gameAll=result.data.gameAll;
				time_temp=result.data.gameTime;
				confirmDiv("此操作将会覆盖您当前的游戏进度,确定要这么做吗?","loadGame");
			}
		});
	}
}
