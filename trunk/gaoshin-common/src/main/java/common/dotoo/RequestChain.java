package common.dotoo;

import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

public class RequestChain {
    private final Stack<OopRequest> requestStack = new Stack<OopRequest>();

    private static String getChainKey() {
        return RequestChain.class.getName();
    }

    private static RequestChain getRequestChain(HttpServletRequest request) {
        RequestChain requestChain = (RequestChain) request.getAttribute(getChainKey());
        if (requestChain == null) {
            requestChain = new RequestChain();
            request.setAttribute(getChainKey(), requestChain);
        }
        return requestChain;
    }

    public static OopRequest begin(HttpServletRequest request) {
        RequestChain requestChain = getRequestChain(request);

        OopRequest oopRequest = new OopRequest();
        String path = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (path == null) {
            path = request.getServletPath();
            oopRequest.setUri(path);
            oopRequest.setParam(request.getQueryString());
        } else {
            oopRequest.setUri(path);
            oopRequest.setParam((String) request.getAttribute("javax.servlet.include.query_string"));
        }

        requestChain.push(oopRequest);

        return oopRequest;
    }

    public static void end(HttpServletRequest request) {
        RequestChain requestChain = getRequestChain(request);
        requestChain.pop();
    }

    /**
     * @param currentRequest
     */
    private void push(OopRequest currentRequest) {
        if (requestStack.empty()) {
            currentRequest.setOopContext(currentRequest.getPath());
        } else {
            OopRequest caller = requestStack.peek();
            String callerContext = caller.getOopContext();
            String currentContext = currentRequest.getPath();
            String newContext = currentContext;
            if (callerContext.indexOf(currentContext) != -1) {
                newContext = callerContext;
            }
            currentRequest.setOopContext(newContext);
        }
        requestStack.push(currentRequest);
    }

    private void pop() {
        requestStack.pop();
    }

}
