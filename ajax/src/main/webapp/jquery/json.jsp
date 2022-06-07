<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jquery - json</title>
<script src="<%= request.getContextPath() %>/js/jquery-3.6.0.js"></script>
<style>
table {
	border : 1px solid #000;
	border-collapse : collapse;
	margin : 10px 0;
}
tr[data-no] {
	cursor : pointer;
}
th, td {
	border : 1px solid #000;
	padding : 5px;
	text-align : center	
}
table img {
	width : 150px;
}
</style>
</head>
<body>
	<h1>json</h1>
	<button id="btn1">전체조회</button>
	<table id="tbl-celeb">
		<thead>
			<tr>
				<th>No</th>
				<th>이름</th>
				<th>타입</th>
				<th>프로필</th>
				<th>삭제</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	<script>
	btn1.addEventListener('click', () => {
		findAll();
	});
	
	const findAll = () => {
		$.ajax({
			url : "<%= request.getContextPath() %>/celeb/findAll",
			method : "GET",
			dataType : "json",
			success(resp) {
				console.log(resp); // js array
				
				const tbody = document.querySelector("#tbl-celeb tbody");
				tbody.innerHTML = "";
				resp.forEach((celeb, index) => {
					console.log(index, celeb);
					const {no, name, type, profile} = celeb;
					const tr = document.createElement("tr");
					// tr[data-no]
					tr.dataset.no = no;
					tr.onclick = function() {
						findOne(this.dataset.no);
					};
					const tdNo = document.createElement("td");
					tdNo.append(no);
					const tdName = document.createElement("td");
					tdName.append(name);
					const tdType = document.createElement("td");
					tdType.append(type);
					const tdProfile = document.createElement("td");
					const img = document.createElement("img");
					img.src = `<%= request.getContextPath() %>/images/\${profile}`;
					tdProfile.append(img);
					
					const tdDel = document.createElement("td");
					const button = document.createElement("button");
					button.textContent = "삭제";
					button.onclick = (e) => {
						e.stopPropagation(); // 이벤트 전파 중지
						deleteCeleb(no);
					};
					
					tdDel.append(button);
					tr.append(tdNo, tdName, tdType, tdProfile, tdDel);
					tbody.append(tr);
				});
			},
			error : console.log
		});
	};
	
	const deleteCeleb = (no) => {
		console.log(no + "삭제 요청!");
	};
	/**
	 * tr 클릭시 해당 no의 celeb 한건을 조회해서 celebUpdateFrm에 세팅
	 */
	const findOne = (no) => {
		$.ajax({
			url : "<%= request.getContextPath() %>/celeb/findOne",
			data : {no},
			success(celeb) {
				const {no, name, type, profile} = celeb;
				const frm = document.celebUpdateFrm;
				frm.no.value = no;
				frm.name.value = name;
				frm.type.value = type;
				frm.querySelector("#celeb-profile").src = `<%= request.getContextPath() %>/images/\${profile}`;
			},
			error : console.log
		});
	};
	</script>
	
	<hr />
	
	<form name="celebUpdateFrm">
		<fieldset>
			<legend>Celeb 수정폼</legend>
			<table>
				<tbody>
					<tr>
						<th>
							<label for="celeb-update-no"No></label>
						</th>
						<td>
							<input type="text" name="no" id="celeb-update-no" readonly/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-update-name">Name</label>
						</th>
						<td>
							<input type="text" name="name" id="celeb-update-name" />
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-update-type">Type</label>
						</th>
						<td>
							<select name="type" id="celeb-update-type">
								<option value="ACTOR">ACTOR</option>
								<option value="SINGER">SINGER</option>
								<option value="MODEL">MODEL</option>
								<option value="COMEDIAN">COMEDIAN</option>
								<option value="ENTERTAINER">ENTERTAINER</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-update-profile">Profile</label>
						</th>
						<td>
							<div>
								<img src="" alt="" id="celeb-profile"/>
							</div>
							<input type="file" name="profile" id="celeb-update-profile" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<button>수정</button>
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>
	<script>
	document.celebUpdateFrm.addEventListener('submit', (e) => {
		e.preventDefault();
		const frm = e.target;
		const frmData = new FormData(frm);
		
		$.ajax({
			url : "<%= request.getContextPath() %>/celeb/update",
			method : "POST",
			data : frmData,
			contentType : false,
			processData : false,
			success(celeb) {
				if(celeb) {
					alert("정보 수정 성공");
					findAll(); // 목록 조회
					frm.reset(); // 폼 초기화
					frm.querySelector("#celeb-profile").src = "";
				}
			},
			error : console.log
		});
	});
	</script>
	
	<hr />
	
	<form name="celebEnrollFrm">
		<fieldset>
			<legend>Celeb 등록폼</legend>
			<table>
				<tbody>
					<tr>
						<th>
							<label for="celeb-enroll-no">No</label>
						</th>
						<td>
							<input type="text" name="no" id="celeb-enroll-no"/>
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-enroll-name">Name</label>
						</th>
						<td>
							<input type="text" name="name" id="celeb-enroll-name" />
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-enroll-type">Type</label>
						</th>
						<td>
							<select name="type" id="celeb-enroll-type">
								<option value="ACTOR">ACTOR</option>
								<option value="SINGER">SINGER</option>
								<option value="MODEL">MODEL</option>
								<option value="COMEDIAN">COMEDIAN</option>
								<option value="ENTERTAINER">ENTERTAINER</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							<label for="celeb-enroll-profile">Profile</label>
						</th>
						<td>
							<input type="file" name="profile" id="celeb-enroll-profile"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<button>등록</button>
						</td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</form>
	<script>
	document.celebEnrollFrm.addEventListener('submit', (e) => {
		e.preventDefault();
		
		// 비동기요청 파일업로드 FormData 객체 사용
		const frmData = new FormData(e.target);
		console.log(frmData.values());
		for(let key of frmData.keys()) {
			console.log(key, frmData.get(key));
		}
		
		$.ajax({
			url : "<%= request.getContextPath() %>/celeb/enroll",
			method : "POST",
			dataType : "json",
			data : frmData,
			processData : false, // 문자열처리하지 않음
			contentType : false, // content type 지정 속성. 기본값 (application/x-www-form-urlencoded) 처리안함
			success(resp) {
				console.log(resp);
				const {result, data} = resp;
				if(result) {
					alert("등록 성공!");
					findAll();
					e.target.reset();
				}
			},
			error : console.log
		})
	});
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