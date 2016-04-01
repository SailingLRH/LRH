<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>101 数独</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link type="image/x-icon" rel="icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="shortcut icon" href="resources/default/img/icon/favicon.ico">
	<link type="image/x-icon" rel="bookmark" href="resources/default/img/icon/favicon.ico">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  	<link rel="stylesheet" type="text/css" href="resources/default/css/game_shudu.css">
	<script type="text/javascript">thisModel=8;gameType=1;gameName="101"</script>
</head>

<body>
	<div class="all">
	    <jsp:include page="../head.jsp"></jsp:include>
	    <jsp:include page="../leftMenu.jsp"></jsp:include>
	    <div class="right_all">
	    	<div class="game_win win">
	    		<div class="win_head">
    				<p class="win_head_icon"><a href="javaScript:void(0)" onclick="goBackIndex()" class="back_btn"></a><p>
    				<p class="win_head_title" ondblclick="successful()">101 数独</p>
	    			
    				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)"></a><p>
    				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
    				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
	    				<div class="game">
							<div class="game_main">
								<div class="court left_top">
									<div class="cell son_left_top row1 column1 init"><p class="number_p">5</p></div>
									<div class="cell son_middle_top row1 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row1 column3 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row2 column1 init"><p class="number_p">8</p></div>
									<div class="cell son_middle_middle row2 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row2 column3 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row3 column1 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_bottom row3 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row3 column3 init"><p class="number_p">4</p></div>
									<div class="clear"></div>
								</div>
								<div class="court middle_top">
									<div class="cell son_left_top row1 column4 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_top row1 column5 init"><p class="number_p">3</p></div>
									<div class="cell son_right_top row1 column6 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row2 column4 init"><p class="number_p">4</p></div>
									<div class="cell son_middle_middle row2 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row2 column6 init"><p class="number_p">9</p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row3 column4 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_bottom row3 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row3 column6 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
								</div>
								<div class="court right_top">
									<div class="cell son_left_top row1 column7 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_top row1 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row1 column9 init"><p class="number_p">4</p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row2 column7 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_middle row2 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row2 column9 init"><p class="number_p">7</p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row3 column7 init"><p class="number_p">6</p></div>
									<div class="cell son_middle_bottom row3 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row3 column9 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
								</div>
								<div class="clear"></div>
								<div class="court left_middle">
									<div class="cell son_left_top row4 column1 init"><p class="number_p">4</p></div>
									<div class="cell son_middle_top row4 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row4 column3 init"><p class="number_p">8</p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row5 column1 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_middle row5 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row5 column3 init"><p class="number_p">7</p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row6 column1 init"><p class="number_p">3</p></div>
									<div class="cell son_middle_bottom row6 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row6 column3 init"><p class="number_p">5</p></div>
									<div class="clear"></div>
								</div>
								<div class="court middle_middle">
									<div class="cell son_left_top row4 column4 init"><p class="number_p">7</p></div>
									<div class="cell son_middle_top row4 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row4 column6 init"><p class="number_p">6</p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row5 column4 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_middle row5 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row5 column6 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row6 column4 init"><p class="number_p">9</p></div>
									<div class="cell son_middle_bottom row6 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row6 column6 init"><p class="number_p">1</p></div>
									<div class="clear"></div>
								</div>
								<div class="court right_middle">
									<div class="cell son_left_top row4 column7 init"><p class="number_p">9</p></div>
									<div class="cell son_middle_top row4 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row4 column9 init"><p class="number_p">1</p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row5 column7 init"><p class="number_p">2</p></div>
									<div class="cell son_middle_middle row5 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row5 column9 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row6 column7 init"><p class="number_p">7</p></div>
									<div class="cell son_middle_bottom row6 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row6 column9 init"><p class="number_p">8</p></div>
									<div class="clear"></div>
								</div>
								<div class="clear"></div>
								<div class="court left_bottom">
									<div class="cell son_left_top row7 column1 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_top row7 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row7 column3 init"><p class="number_p">3</p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row8 column1 init"><p class="number_p">9</p></div>
									<div class="cell son_middle_middle row8 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row8 column3 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row9 column1 init"><p class="number_p">7</p></div>
									<div class="cell son_middle_bottom row9 column2 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row9 column3 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
								</div>
								<div class="court middle_bottom">
									<div class="cell son_left_top row7 column4 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_top row7 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row7 column6 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row8 column4 init"><p class="number_p">8</p></div>
									<div class="cell son_middle_middle row8 column5 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row8 column6 init"><p class="number_p">3</p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row9 column4 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_bottom row9 column5 init"><p class="number_p">4</p></div>
									<div class="cell son_right_bottom row9 column6 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
								</div>
								<div class="court right_bottom">
									<div class="cell son_left_top row7 column7 init"><p class="number_p">1</p></div>
									<div class="cell son_middle_top row7 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_top row7 column9 normal"><p class="number_p"></p></div>
									<div class="clear"></div>
									<div class="cell son_left_middle row8 column7 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_middle row8 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_middle row8 column9 init"><p class="number_p">2</p></div>
									<div class="clear"></div>
									<div class="cell son_left_bottom row9 column7 normal"><p class="number_p"></p></div>
									<div class="cell son_middle_bottom row9 column8 normal"><p class="number_p"></p></div>
									<div class="cell son_right_bottom row9 column9 init"><p class="number_p">9</p></div>
									<div class="clear"></div>
								</div>
								<div class="clear"></div>
							</div>
	    				</div>
						<!-- over -->
	    			</div>
	    		</div>
	    		
	    		<div class="win_bottom">
	    			<!-- 游戏按钮 -->
	    			<div class="game_btn">
	    				<p class="play_or_stop">
	    					<a class="play play_able" title="点击开始游戏" href="javaScript:void(0)" onclick="play()"></a>
	    				</p>
	    				
	    				<p>
	    					<a class="clear_btn clear_btn_able" title="清除选中的数字" href="javaScript:void(0)" onclick="clearCell()"></a>
	    				</p>
	    				
	    				<p>
	    					<a class="replay replay_able" title="重新开始" href="javaScript:void(0)" onclick="replay()"></a>
	    				</p>
	    				
	    				<c:choose>
		    				<c:when test="${!empty sessionScope.user}">
			    				<p><a class="save save_able" title="保存进度" onclick="saveGame()" href="javaScript:void(0)"></a></p>
			    				<p><a class="load load_able" title="载入进度" onclick="loadGame(0)" href="javaScript:void(0)"></a></p>
		    				</c:when>
		    				<c:otherwise>
			    				<p><a class="save" title="登录后才能保存进度" onclick="alertDiv('您需要先登录才能保存进度哦~')"></a></p>
			    				<p><a class="load" title="登录后才能载入进度" onclick="alertDiv('您需要先登录才能载入进度哦~')"></a></p>
		    				</c:otherwise>
		    			</c:choose>
	    				
	    				<div class="clear"></div>
	    			</div>
	    			
	    			<!-- 游戏信息 -->
	    			<div class="game_info">
	    				<p><span class="lable game_process_lable"></span><span class="game_process">0.00%</span></p>
	    				<p><span class="lable game_time_lable"></span><span class="game_time">00:00:00</span></p>
	    				<p><span class="lable game_status_lable"></span><span class="game_status">未开始</span></p>
	    				<p><span class="lable game_level_lable"></span><span class="game_level">中级</span></p>
	    				
	    				<div class="clear"></div>
	    			</div>
	    			
	    			<div class="clear"></div>
	    		</div>
	    	</div>
	    	
	    	<div class="win win_parting_line"></div>
		    <!-- 游戏规则 -->
		    <div class="game_rule win">
	    		<div class="win_head">
	   				<p class="win_head_icon"><p>
	   				<p class="win_head_title">游戏规则及玩法简述</p>
	    			
	   				<p class="win_controll_close_btn win_controll_btn"><a href="javaScript:void(0)"></a><p>
	   				<p class="win_controll_max_btn win_controll_btn"><a href="javaScript:void(0)"></a></p>
	   				<p class="win_controll_min_btn win_controll_btn"><a href="javaScript:void(0)" class="able"></a><p>
	    			
	    			<div class="clear"></div>
	    		</div>
	    		
	    		<div class="win_body">
	    			<div class="win_body_all">
	    				<p class="text_p">
							通过给出的数字推理出其余空格的数字,要求每行,每列,每宫都要出现1~9.其实就是每行,每列,每宫中的数字都不能重复.
						</p>
						<p class="text_p">
							玩法很简单,先点击右边的开始按钮,然后点击数独上的空格,在被点击的空格下面会出现1~9备选数字,点击备选数字就能将数字填入空格了.
						</p>
						<p class="text_p">
							快捷键操作:&nbsp;&nbsp;按下回车键开始/暂停游戏;选中单元格,按下"-"(减号)键清除被选中单元格中的数字;按下F5重新开始游戏.
						</p>
	    			</div>
	    		</div>
	    		<div class="win_bottom"></div>
	    	</div>
	    	
	    </div>
	    
	 </div>
</body>
<script type="text/javascript" src="resources/default/js/game_shudu.js"></script>
</html>
