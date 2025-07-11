package com.lxt.test.config;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * mcp配置
 */
@Configuration
public class McpConfig {
    @Bean
    public McpClient baiduMcpClient() {
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://mcp.map.baidu.com/sse?ak=euRsBs0TjmUfEwDgC4qLhzOK")
                .logRequests(true) // if you want to see the traffic in the log
                .logResponses(true)
                .build();

        return new DefaultMcpClient.Builder()
                .key("baiduMcpClient")
                .transport(transport)
                .build();
    }

    /**
     * 文件系统client ,此服务需要时基于nodejs开发的，所以需要nodejs环境，代码中需要判断是否是windows系统，如果是则使用cmd命令，否则使用bash命令
     *
     * @return client
     */
    @Bean
    public McpClient fileSystemMcpClient() {
        StdioMcpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("cmd", "/c", "npx", "-y", "@modelcontextprotocol/server-filesystem", "D:\\companyCode\\test"))
                .logEvents(true)
                .build();
        return new DefaultMcpClient.Builder()
                .transport(transport)
                .build();
    }


    @Bean
    public ToolProvider mcpToolProvider() {
        return McpToolProvider.builder()
                .mcpClients(fileSystemMcpClient(),baiduMcpClient())
                .build();
    }




//    @Bean
//    public McpClient baiduMcpClient() {
//        // 百度地图client
//        StdioMcpTransport transport = new StdioMcpTransport.Builder()
//                .command(List.of("cmd", "/c", "npx", "-y", "@baidumap/mcp-server-baidu-map"))
//                .environment(Map.of("BAIDU_MAP_API_KEY","euRsBs0TjmUfEwDgC4qLhzOK"))
//                .logEvents(true)
//                .build();
//        return new DefaultMcpClient.Builder()
//                .transport(transport)
//                .build();
//    }
}
