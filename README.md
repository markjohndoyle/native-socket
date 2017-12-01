# Native socket

## Usage

```
private Socket yourSocket;
.
.
public void yourMethod()
{
    NativeSocket nativeSocket = NativeSocketFactory.createFrom(yourSocket);
    .
    .
    nativeSocket.setKeepAliveInterval(Duration.standardSeconds(1));
}
```