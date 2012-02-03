//Headers
//#include <WinSock2.h>
//#include <Windows.h>
//#include <stdio.h>
//#include "tinystr.h"
//#include "tinyxml.h"
#include "StdAfx.h"
#include "UDPServer.h"
#pragma comment(lib,"Ws2_32.lib")

int UDPServer::startWinSock(){
	WSADATA wsa;
	return WSAStartup(MAKEWORD(2,0),&wsa);
}

void UDPServer::sendMsg(char* msg){
	
	//buf = printer.CStr();
	//const char* buf2 = buf.c_str();
	send(s,msg,strlen(msg),0);
}

UDPServer::UDPServer(){
	
	const u_short port=2020;

	//Start Winsock
	rc = startWinSock();
	if(rc != 0){
		printf("Error: startWinsock, Error code: %d\n",rc);
    return; 
	}

	//Create Socket
	s = socket(AF_INET,SOCK_DGRAM,0);
	if(s== INVALID_SOCKET){
		printf("Error: Socket could´nt be created");
		return;
	}

	memset(&addr,0,sizeof(SOCKADDR_IN));
	addr.sin_family = AF_INET;
	addr.sin_port = htons(port);
	printf("Started Server on port %d" ,port);

	addr.sin_addr.s_addr = inet_addr("127.0.0.1");
	rc = connect(s,(SOCKADDR*)&addr,sizeof(SOCKADDR_IN));
	if(rc == SOCKET_ERROR){
		printf("Error: bind, Error code: %d\n",rc);
		return;
	}

}
