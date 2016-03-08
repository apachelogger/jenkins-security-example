public class MyUser implements UserDetails {
    private String name;
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

    private KDEConduitUser(name) {
        super();
        this.name = name;
        authorities.add(SecurityRealm.AUTHENTICATED_AUTHORITY);
    }

    public void addAuthority(GrantedAuthority authority) {
        authorities.add(authority);
    }

    @Override
    public GrantedAuthority[] getAuthorities() {
        return authorities.toArray(new GrantedAuthority[authorities.size()]);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
