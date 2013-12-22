<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
"cols":
    [
        <%
            Object[][] columns = (Object[][]) pageContext.findAttribute("columns");
            for(int i = 0; i < columns.length; i++) {
                out.print("{\"id\":\""+i+"\",\"label\":\""+columns[i][0]+"\",\"type\":\"number\"}");
                if(i+1<columns.length) {
                    out.println(",");
                }
            }
        %>
    ],
"rows":[
<%
    for(int i = columns[0].length-1; i > 0; i--) {
        out.print("{\"c\":[");
        for(int j = 0; j < columns.length; j++) {
            if(columns[j][i]==null) {
                columns[j][i] = 0;
            }
            Integer value = 0;
            if(columns[j][i] instanceof Integer) {
                value = (Integer) columns[j][i];
            } else if(columns[j][i] instanceof Long) {
                value = ((Long) columns[j][i]).intValue();
            }
            if(j==0) {
                value = columns[0].length-i;

            }
            out.print("{\"v\":\""+value+"\"}");
            if(j+1<columns.length) {
                out.print(",");
            }
        }
        out.println("]}");
        if(i-1<columns[0].length) {
            out.print(",");
        }
    }
%>
]
}