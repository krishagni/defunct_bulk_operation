<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/nlevelcombo.tld" prefix="ncombo" %>

<%@ page import="edu.wustl.bulkoperator.util.BulkOperationConstants"%>
<%@ page import="java.util.*"%>

<SCRIPT>var imgsrc="images/";</SCRIPT>
<script src="jss/script.js" type="text/javascript"></script>

	<%	
		String pageOf = (String)request.getAttribute(BulkOperationConstants.PAGE_OF);
		String participantId=(String)request.getAttribute("participantId");
		String cpId=(String)request.getAttribute("cpId");
		if(pageOf != null && pageOf.equals(BulkOperationConstants.PAGE_OF_PARTICIPANT_CP_QUERY))
		{
	%>
	<script language="javascript">
			<%
				if(participantId != null)
				{
			%>
			window.parent.frames['<%=BulkOperationConstants.CP_AND_PARTICIPANT_VIEW%>'].location="showCpAndParticipants.do?cpId=<%=cpId%>&participantId=<%=participantId%>";
			window.parent.frames['<%=BulkOperationConstants.CP_TREE_VIEW%>'].location="showTree.do";
			
			<%
				}
				else
				{
			%>
			window.parent.frames['<%=BulkOperationConstants.CP_AND_PARTICIPANT_VIEW%>'].location="showCpAndParticipants.do?cpId=<%=cpId%>";
			<%
				}
			%>
	</script>
	<%
		}
	%>