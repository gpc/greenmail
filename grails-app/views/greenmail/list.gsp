<!--
  Copyright 2011 the original author or authors.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<%@ page import="com.icegreen.greenmail.util.GreenMailUtil" %>
<head>
	<!--<meta name="layout" content="main" />-->
	<title>Email List</title>

    <style type="text/css">
        table.list { width: 100%; margin-bottom: 12px; border: 1px solid #000; text-align: left; }
        table.list caption { padding-top: 6px; text-align: left; font-size: 12px; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; }
        table.list tr.odd { background: #fbfbfb; }
        table.list th, table.list td { padding: 6px; border-right: 1px solid #000; }
        table.list th { border-bottom: 1px solid #000; vertical-align: top; background: #eee; line-height: 100%;}
        table.list td { border-bottom: 1px dotted #aaa; vertical-align: top; }
    </style>
</head>

<body>
	<div class="body">
		<h2>Email List</h2>
		<div>
			<table class="list">
			<thead>
				<tr>
					<th>id</th>
					<th>Sent</th>
					<th>Subject</th>
					<th>Recipients</th>
					<th>&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			<g:each in="${list}" status="i" var="email">
				<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
					<td>${i}</td>
					<td>${email.sentDate}</td>
					<td>${email.subject.encodeAsHTML() }</td>
					<td>${GreenMailUtil.getAddressList(email.getRecipients(javax.mail.Message.RecipientType.TO))}</td>
					<td class="actionButtons">
						<span class="actionButton">
							<g:link action="show" id="${i}">Show</g:link>
						</span>
					</td>
				</tr>
			</g:each>
			</tbody>
			</table>
		</div>

	</div>
</body>
