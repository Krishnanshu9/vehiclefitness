// wrap in an IIFE and attach to window
(function(window) {
  const AuthHelper = {
    isAuthenticated() {
      return !!localStorage.getItem('authToken');
    },
    getAuthToken() {
      return localStorage.getItem('authToken');
    },
    getCurrentUser() {
      return {
        email:   localStorage.getItem('userEmail'),
        fullName: localStorage.getItem('userName')
      };
    },
    addAuthHeader(options = {}) {
      const token = this.getAuthToken();
      if (!token) return options;
      options.headers = options.headers || {};
      options.headers['Authorization'] = `Bearer ${token}`;
      return options;
    },
    authFetch: async function(url, options = {}) {
      const authOptions = this.addAuthHeader(options);
      try {
        const response = await fetch(url, authOptions);
        if (response.status === 401) {
          console.log('Session expired, logging out');
          this.logout();
          throw new Error('Session expired');
        }
        return response;
      } catch (e) {
        console.error('authFetch error:', e);
        throw e;
      }
    },
    logout() {
      console.log('Logout clicked – clearing storage and redirecting');
      localStorage.removeItem('authToken');
      localStorage.removeItem('userEmail');
      localStorage.removeItem('userName');
      window.location.href = 'index.html';
    },
    protectPage() {
      const path = window.location.pathname;
      const isLogin      = path.endsWith('login.html');
      const isRegister   = path.endsWith('register.html');
      const isHome       = path === '/' || path.endsWith('index.html');
      if (!this.isAuthenticated() && !(isLogin || isRegister || isHome)) {
        console.log('Not authenticated – redirecting to login');
        window.location.href = 'login.html';
      }
    },
    initialize() {
      console.log('AuthHelper.initialize()');
      this.protectPage();
      const btn = document.getElementById('logout-btn');
      if (btn) {
        btn.addEventListener('click', () => this.logout());
        console.log('Logout button listener attached');
      } else {
        console.log('No #logout-btn on this page');
      }
    }
  };

  window.AuthHelper = AuthHelper;
  // initialize after DOM is parsed
  document.addEventListener('DOMContentLoaded', () => AuthHelper.initialize());
})(window);
