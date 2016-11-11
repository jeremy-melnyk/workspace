package udp;

import enums.UdpRequestType;

public class Request {
	private UdpRequestType requestType;
	private String data;

	public Request(UdpRequestType requestType, String data) {
		super();
		this.requestType = requestType;
		this.data = data;
	}

	public UdpRequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(UdpRequestType requestType) {
		this.requestType = requestType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
