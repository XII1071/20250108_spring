# 🏗️ SpringBoot Architecture
<br>
<div>
    <h4>📦 Spring Boot Project Structure</h4>
    <pre class="tree">
📦 <span class="folder">com.example.project</span>
 ┣ 📂 <span class="folder">controller</span>&nbsp;&nbsp;&nbsp;      <b>(1) 컨트롤러 계층</b>
 ┣ 📂 <span class="folder">service</span>&nbsp;&nbsp;          <b>(2) 서비스 계층</b>
 ┣ 📂 <span class="folder">repository</span>&nbsp;&nbsp;       <b>(3) 데이터 접근 계층</b>
 ┣ 📂 <span class="folder">domain (model)</span>&nbsp;&nbsp;   <b>(4) 도메인/엔티티 계층</b>
 ┣ 📂 <span class="folder">dto</span>&nbsp;&nbsp;              <b>(5) DTO 계층</b>
 ┣ 📂 <span class="folder">config</span>&nbsp;&nbsp;           <b>(6) 설정 관련 패키지</b>
 ┗ 📜 <span class="file">Application.java</span>   <b>메인 클래스 (Spring Boot 실행)</b>  
</div>
</pre>
<br>
<b>🔥 Spring Boot 전체 흐름을 표현한 다이어그램</b>

<img src="img/dtoentity.png" />
