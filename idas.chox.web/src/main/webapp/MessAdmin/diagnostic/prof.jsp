<%@page session="false" contentType="text/html; charset=utf-8"
%><%@ page import="clime.messadmin.profiler.SamplingProfiler"
%><%@taglib prefix="core" uri="http://messadmin.sf.net/core"
%><%@taglib prefix="format" uri="http://messadmin.sf.net/fmt"
%><%!
	private static volatile SamplingProfiler prof;
%>
<%--!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"--%>
<%--!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"--%>
<%--!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN"
     "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"--%>
<html lang="<%= response.getLocale() %>">
<format:setBundle basename="clime.messadmin.admin.i18n.profiler"/>
<head>
	<%@ include file="../inc/meta.inc" %>
	<title><format:message key="page.title"></format:message></title>
	<%@ include file="../inc/css.inc" %>
	<style type="text/css">
	</style>
	<script type="text/javascript">//<![CDATA[
	//]]>
	</script>
</head>
<body>
<h2>CPU Sampling Profiling Tool</h2>
<p>
Collects statistics about thread dumps similar to <code>java -agentlib:hprof</code>.<br>
Stack traces from runnable threads are then listed sorted by count.
</p>
<%
	final String action = request.getParameter("profiler_action");
	String disabledAttr = "";
	int samplingInterval = 100;
	String ignoredThreadsList = request.getParameter("ignoredThreadsList");
	String ignoredPackagesList = request.getParameter("ignoredPackagesList");
	String formAction = "";
	String submitLabel = "";
	try {
		samplingInterval = Integer.parseInt(request.getParameter("samplingInterval"));
	} catch (NumberFormatException ignore) {
	}
	if (ignoredThreadsList == null) {
		ignoredThreadsList  = "Reference Handler\n" +
			"Finalizer\n" +
			"Signal Dispatcher\n" +
			"RMI TCP Accept-\n";
	}
	if (ignoredPackagesList == null) {
		ignoredPackagesList = "java.lang.Object#wait\n" +
			"java.lang.Thread#sleep\n" +
			"java.lang.Thread#dumpThreads\n" +
			"java.net.PlainSocketImpl#accept\n" +
			"java.net.PlainSocketImpl#socketAccept\n" +
			"java.net.SocketInputStream#socketRead\n" +
			"java.net.SocketOutputStream#socketWrite\n" +
			"sun.misc.Unsafe#park\n" +
			"EDU.oswego.\n";
	}
	if (action == null || "stop".equals(action)) {
		disabledAttr = "";
		formAction = "start";
		submitLabel = "Start collecting";
	} else if ("start".equals(action)) {
		disabledAttr = " readonly=\"readonly\"";
		formAction = "stop";
		submitLabel = "Stop";
	}
	%>
	<form method="post">
		<p>
		<label>Sample interval (ms): <input type="number" name="samplingInterval" min="10" step="5" required="required" value="<%=samplingInterval%>" size="5"<%=disabledAttr%>></label><br>
		<span style="font-size: smaller">To increase accuracy, use 1000 or higher when running for many hours.</span>
		</p><p>
		<label>Ignored threads names:<br>
		<textarea cols="50" rows="4" name="ignoredThreadsList"<%=disabledAttr%>><core:out value="<%= ignoredThreadsList %>"/></textarea></label><br>
		<span style="font-size: smaller">Threads which name <em>starts</em> with an element in this list are ignored.</span>
		</p><p>
		<label>Ignored packages and FQN classes:<br>
		<textarea cols="50" rows="8" name="ignoredPackagesList"<%=disabledAttr%>><core:out value="<%= ignoredPackagesList %>"/></textarea></label><br>
		<span style="font-size: smaller">Stack traces with a line that <em>starts</em> with one of these prefixes will not be included.</span>
		</p>
		<input type="hidden" name="profiler_action" value="<%=formAction%>">
		<input type="submit" value="<%=submitLabel%>">
	</form>
	<%
	if (action == null || "stop".equals(action)) {
		SamplingProfiler old = prof;
		prof = null;
		if (old != null) {
			old.dump(out, 15);
			old.close();
		}
	} else if ("start".equals(action)) {
		%>
		<p>Collecting...</p>
		<%
		out.flush();
		prof = new SamplingProfiler();
		prof.addAllIgnoredThreadNames(ignoredThreadsList, "\\s");
		prof.addAllIgnoredPackagesInStackTrace(ignoredPackagesList, "\\s");
		while (prof != null) {
			prof.profileSample(out);
			try {
				Thread.sleep(samplingInterval);
			} catch (InterruptedException ignore) {
			}
		}
	} else {
%>Unknown action: <core:out value="<%= action %>"/>
<% } %>
<%--jsp:include page="inc/footer.jsp"/--%>
</body>
</html>
