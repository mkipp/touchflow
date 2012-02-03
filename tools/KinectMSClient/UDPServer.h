//Headers
#include <WinSock2.h>
#include <Windows.h>

#include <stdio.h>
#include <tchar.h>
#include <string>


class UDPServer{
		int startWinSock(void);
		long rc;
		SOCKET s;
		SOCKADDR_IN addr; 
		std::string buf;

	public:
		UDPServer();
		void sendMsg(char* msg);
};