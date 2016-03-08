public class MyToken extends AbstractAuthenticationToken {
    private MyUser user;

    public MyToken(String accessToken) {
        user = new MyUser(accessToken, "fakeUser");
        setAuthenticated(user != null);
        user.addAuthority(new GrantedAuthorityImpl("fakeGroup"));
    }

    public MyUser getUser() {
        return user;
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return user != null ? user.getAuthorities() : new GrantedAuthority[0];
    }

    @Override
    public Object getCredentials() {
        return StringUtils.EMPTY;
    }

    @Override
    public Object getPrincipal() {
        return getName();
    }

    @Override
    public String getName() {
        return (user != null ? user.getUsername() : null);
    }
}
