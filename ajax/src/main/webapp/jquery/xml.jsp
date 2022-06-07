<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jquery - xml</title>
<script src="<%= request.getContextPath() %>/js/jquery-3.6.0.js"></script>
<style>
table {border : 1px solid #000; border-collapse: collapse; margin: 10px 0;}
th, td {border : 1px solid #000; text-align: center; padding: 3px; }
table img {width: 100px;}
</style>
<script>
window.addEventListener('load', () => {
	const today = new Date(2022, 5, 1);
	const yesterday = new Date(today.getFullYear(), today.getMonth(), today.getDate() - 1);
	
	// console.log('yesterday = ' + yesterday.toISOString()); 
	const f = (d) => {
		const m = (n) => n < 10 ? "0" + n : n;
		return `\${d.getFullYear()}\${m(d.getMonth() + 1)}\${m(d.getDate())}`
	};
	getDailyBoxOffice(f(yesterday));
})
</script>
</head>
<body>
	<h1>xml</h1>
	<button id="btn1">sample</button>
	<table id="tbl-books">
		<thead>
			<tr>
				<th>분류</th>
				<th>제목</th>
				<th>저자</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
	btn1.addEventListener('click', () => {
		$.ajax({
			url : "<%= request.getContextPath() %>/jquery/sample.xml",
			method : "GET",
			dataType : "xml",
			success(doc) {
				// 응답받은 xml을 jquery가 parsing 처리 후 DOM으로 전달
				console.log(doc);
				
				const root = doc.querySelector(":root");
				// 속성 가져오기
				console.log(root.myattr); // undefined root.attributes(NamedNodeMap타입)속서에서 관리됨.
				console.log(root.getAttribute("myattr")); // hello
				
				const tbody = document.querySelector("#tbl-books tbody");
				tbody.innerHTML = "";
				const books = [...root.children]; // Array.from(유사배열)
				books.forEach((book) => {
					const [subject , title, author] = book.children;
					console.log(subject, title, author);
					
					tbody.innerHTML += `<tr>
						<td>\${subject.textContent}</td>
						<td>\${title.textContent}</td>
						<td>\${author.textContent}</td>
					</tr>`;
				});
			},
			error : console.log
		});
	});
	</script>
	
	<hr />
	
	<button id="btn2">celeb</button>
	<table id="tbl-celebs">
		<thead>
			<tr>
				<th>No</th>
				<th>이름</th>
				<th>타입</th> <!-- select태그 하위에 해당타입이 selected 처리 -->
				<th>프로필</th> <!-- img 태그 처리 -->
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
	btn2.addEventListener('click', () => {
		$.ajax({
			url : "<%= request.getContextPath() %>/jquery/xml",
			// dataType : "xml", // 응답 데이터를 보고 자동으로 지정
			success(doc) {
				const root = doc.querySelector(":root"); // celebs 태그
				console.log(`총 \${root.getAttribute("len")}개의 데이터가 조회되었습니다.`);
				
				const celebs = [...root.children];
				document.querySelector("#tbl-celebs tbody").innerHTML = 
					celebs.reduce((html, celeb) => {
						const [name, type, profile] = celeb.children; // iterator를 상속(진짜배열, 유사배열)
						const tr = `<tr>
							<td>\${celeb.getAttribute("no")}</td>
							<td>\${name.textContent}</td>
							<td>\${type.textContent}</td>
							<td>
								<img src="<%= request.getContextPath() %>/images/\${profile.textContent}" />
							</td>
						</tr>`;
						return html + tr;
					}, "");
			},
			error : console.log
		});
	});
	</script>
	
	<hr />
	<h2>일일 박스오피스 조회</h2>
	<div><input type="date" name="targetDt" id="targetDt" /></div>
	<table id="tbl-daily-boxoffice">
		<thead>
			<tr>
				<th>순위</th>
				<th>영화제목</th>
				<th>누적관객수(만)</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	
	<script>
	/**
	 * @실습문제 - 페이지로딩이 완료되면 어제 날짜로 박스오피스 조회를 자동으로 처리
	 */
	targetDt.addEventListener('change', (e) => {
		const val = e.target.value.replace(/-/g, "");
		getDailyBoxOffice(val);
		
	});
	
	/**
	 * date는 yyyymmdd 포맷을 가진 문자열이다.
	 */
	const getDailyBoxOffice = (date) => {
		$.ajax({
			url : "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.xml",
			data : {
				key : "1b857512787e96eb83c166b0dad6c75e",
				targetDt : date
			},
			success(doc) {
				console.log(doc);
				const root = doc.querySelector(":root");
				const dailyBoxOfficeList = root.lastElementChild;
				const dailyBoxOffices = [...dailyBoxOfficeList.children];
				
				document.querySelector("#tbl-daily-boxoffice tbody").innerHTML =
					dailyBoxOffices.reduce((html, dailyBoxOffice) => {
						const [rnum, rank, rankInten, rankOldAndNew, movieCd, movieNm, openDt, salesAmt, salesShare, salesInten,
							   salesChange, salesAcc, audiCnt, audiInten, audiChange, audiAcc, scrnCnt, showCnt] = dailyBoxOffice.children;
						return `\${html}
						<tr>
							<td>\${rank.textContent}</td>
							<td>\${movieNm.textContent}</td>
							<td>\${audiAcc.textContent}</td>
						</tr>`;
					}, "");
				
			},
			error : console.log
		});
	};
	</script>
	
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
	<br />
</body>
</html>