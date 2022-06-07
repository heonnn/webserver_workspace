<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>XMLHttpRequest</title>
</head>
<body>
	<h1>XMLHttpRequest</h1>
	<h2>GET</h2>
	<form name="searchFrm">
		<input type="search" name="q" id="q" />
		<button>검색</button>
	</form>
	<div id="result"></div>
	<script>
	let xhr; // XMLHttpRequest 객체를 대입할 변수
	document.searchFrm.addEventListener('submit', (e) => {
		e.preventDefault(); // 제출금지. 폼제출 동기요청
		
		// 1. XMLHttpRequest 객체 생성
		xhr = new XMLHttpRequest();
		console.log(xhr);
		
		// 2. xhr 객체 연결상태 변화에 따른 핸들러함수 바인딩
		xhr.onreadystatechange = readyStateChangeHandler;
		
		// 3. open 연결
		// open(전송방식, url?querystring)
		xhr.open("GET", "<%= request.getContextPath() %>/vanilla-js/search?q=" + e.target.q.value);
		
		// 4. send 요청 전송
		xhr.send();
	});
	
	const readyStateChangeHandler = (e) => {
		switch(xhr.readyState) {
		case 0 : console.log('0 : uninitialized'); break; // xhr 객체 생성. 핸들러 호출하지 않음
		case 1 : console.log('1 : loading'); break; // open 연결 중
		case 2 : console.log('2 : loaded'); break; // send 요청
		case 3 : console.log('3 : interactive'); break; // 응답메세지 수신시작
		case 4 : console.log('4 : completed'); break; // 응답메세지 수신완료
		}
		
		if(xhr.readyState === 4) {
			if(xhr.status === 200) {
				// 정상처리
				console.log(xhr.responseText);
				result.innerHTML = xhr.responseText;
			} else {
				// 오류
				alert("오류가 발생했습니다.");
			}
		}
	};
	</script>
	
	<h2>POST</h2>
	<form name="signupFrm">
		<input type="text" name="username" placeholder="username" />
		<input type="email" name="email" placeholder="email" />
		<button>등록</button>
	</form>
	<script>
	document.signupFrm.addEventListener('submit', (e) => {
		e.preventDefault(); // 폼제출 방지
		
		// 1. XMLHttpRequest 객체 생성
		xhr = new XMLHttpRequest();
		
		// 2. readystatechange 핸들러 바인딩
		xhr.onreadystatechange = signupHandler;
		
		// 3. open 연결 생성 - content-type 지정(필수)
		xhr.open("POST", "<%= request.getContextPath() %>/vanilla-js/signup");
		xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
		
		// 4. send 요청 - 사용자 입력값 전달 (message body에 작성)
		const usernameVal = e.target.username.value;
		const emailVal = e.target.email.value;
		const param = `username=\${usernameVal}&email=\${emailVal}`;
		
		xhr.send(param); // 데이터 직렬화 처리 후 전송
	});
	
	const signupHandler = (e) => {
		if(xhr.readyState === 4 && xhr.status === 200) {
			// 정상처리
			alert(xhr.responseText);
		}
		if(xhr.readyState === 4 && xhr.status !== 200) {
			// 오류처리
			console.log(xhr);
			result.innerHTML = xhr.responseText;
		}
	};
	</script>

</body>
</html>