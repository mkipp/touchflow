xinput --set-prop $1 272 10
stdbuf -i0 -o0 -e0 xinput test $1|telnet localhost 1235
