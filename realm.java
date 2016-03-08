public class MySecurityRealm extends SecurityRealm implements UserDetailsService {
    private MySecurityRealm() { }

    @Override
    public String getLoginUrl() { return "securityRealm/commenceLogin"; }

    public HttpResponse doCommenceLogin(StaplerRequest request, @Header("Referer") final String referer)
            throws IOException {
        // Building OAuth request URI happens here...
        return new HttpRedirect(requestURI);
    }

    public HttpResponse doFinishLogin(StaplerRequest stablerRequest)
            throws IOException, OAuthProblemException, OAuthSystemException {
        // Use oauth code handed over by remote here...
        // ...and request an access token from remote...

        if (accessToken != null && accessToken.trim().length() > 0) {
            // Build an auth token here...
            auth = MyAuthToken.new(accessToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityListener.fireAuthenticated(auth.getUser());
        }
        return HttpResponses.redirectToContextRoot();
    }

    @Override
    public boolean allowsSignup() { return false; }

    @Override
    public SecurityComponents createSecurityComponents() {
        return new SecurityComponents(new AuthenticationManager() {
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                if (authentication instanceof MyAuthToken) {
                    return authentication;
                }
                throw new BadCredentialsException("unexpected type: " + authentication);
            }
        }, new UserDetailsService() {
            public UserDetails loadUserByUsername(String username)
                    throws UsernameNotFoundException {
                return MySecurityRealm.this.loadUserByUsername(username);
            }
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        UserDetails user = null;
        MyAuthToken authToken = (MyAuthToken) SecurityContextHolder.getContext().getAuthentication();
        if (authToken == null) {
            // Need to throw MayOrMayNot here. This allows impersination which
            // is how API tokens manage to associated with a user while they
            // actually have no session running.
            throw new UserMayOrMayNotExistException("no token -> cannot establish user: " + username);
        }
        user = authToken.getUser();
        if (user == null) {
            throw new UsernameNotFoundException("user not found on phab: " + username);
        }
        return user;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        @Override
        public String getDisplayName() {
            return "My Authentication";
        }
    }
}
