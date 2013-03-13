package proxy.net;

import javax.servlet.http.Cookie;

public class UniqueCookie extends Cookie {

	private static final long serialVersionUID = 6265300061884647488L;

	public UniqueCookie(String name, String value) {
		super(name, value);

	}

	@Override
	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof Cookie)) {
			return false;
		}

		UniqueCookie uniqueCookie = (UniqueCookie) obj;

		if (uniqueCookie.getDomain().equals(this.getDomain())
				&& uniqueCookie.getName().equals(this.getName())
				&& uniqueCookie.getValue().equals(this.getValue())
				&& uniqueCookie.getPath().equals(this.getPath())
				&& uniqueCookie.getVersion() == this.getVersion()) {
			return true;
		}else{
			return false;
		}

	}

}
