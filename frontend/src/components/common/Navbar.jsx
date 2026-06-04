import { useState, useEffect } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useTheme } from '../../context/ThemeContext'
import API from '../../services/api'
import { FiShoppingCart, FiUser, FiMenu, FiX, FiZap, FiHeart, FiSun, FiMoon } from 'react-icons/fi'
import { MdAdminPanelSettings } from 'react-icons/md'

const Navbar = () => {
  const { user, logout, isAdmin } = useAuth()
  const { isDark, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [menuOpen, setMenuOpen] = useState(false)
  const [cartCount, setCartCount] = useState(0)
  const [wishlistCount, setWishlistCount] = useState(0)

  useEffect(() => {
    if (user && user.id) {
      fetchCartCount()
      fetchWishlistCount()
    } else {
      setCartCount(0)
      setWishlistCount(0)
    }

    const handleCartUpdate = () => { if (user) fetchCartCount() }
    const handleWishlistUpdate = () => { if (user) fetchWishlistCount() }

    window.addEventListener('cartUpdated', handleCartUpdate)
    window.addEventListener('wishlistUpdated', handleWishlistUpdate)
    return () => {
      window.removeEventListener('cartUpdated', handleCartUpdate)
      window.removeEventListener('wishlistUpdated', handleWishlistUpdate)
    }
  }, [user])

  const fetchCartCount = async () => {
    try {
      const res = await API.get(`/api/cart/user/${user.id}`)
      setCartCount(res.data?.cartItems?.length || 0)
    } catch (err) { setCartCount(0) }
  }

  const fetchWishlistCount = async () => {
    try {
      const res = await API.get(`/api/wishlist/${user.id}`)
      setWishlistCount(res.data?.length || 0)
    } catch (err) { setWishlistCount(0) }
  }

  const handleLogout = () => {
    logout()
    navigate('/')
    setMenuOpen(false)
    setCartCount(0)
    setWishlistCount(0)
  }

  const navBg = isDark ? 'bg-gray-900 border-gray-800' : 'bg-white border-gray-200 shadow-sm'
  const linkClass = isDark
    ? 'text-gray-300 hover:text-red-500 transition-colors'
    : 'text-gray-600 hover:text-red-500 transition-colors'

  return (
    <nav className={`${navBg} border-b sticky top-0 z-50`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">

          <Link to="/" className="flex items-center gap-2">
            <FiZap className="text-red-500 text-2xl" />
            <span className={`text-xl font-bold ${isDark ? 'text-white' : 'text-gray-900'}`}>
              Electro<span className="text-red-500">Mart</span>
            </span>
          </Link>

          <div className="hidden md:flex items-center gap-8">
            <Link to="/" className={linkClass}>Home</Link>
            <Link to="/products" className={linkClass}>Products</Link>
            <Link to="/contact" className={linkClass}>Contact</Link>
          </div>

          <div className="hidden md:flex items-center gap-4">
            {/* Theme Toggle */}
            <button
              onClick={toggleTheme}
              className={`p-2 rounded-lg transition-colors ${isDark ? 'text-gray-400 hover:text-yellow-400 hover:bg-gray-800' : 'text-gray-500 hover:text-blue-500 hover:bg-gray-100'}`}
              title={isDark ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
            >
              {isDark ? <FiSun className="text-xl" /> : <FiMoon className="text-xl" />}
            </button>

            {user ? (
              <>
                {isAdmin() && (
                  <Link to="/admin" className="flex items-center gap-1 text-yellow-400 hover:text-yellow-300 transition-colors">
                    <MdAdminPanelSettings className="text-xl" />
                    <span className="text-sm">Admin</span>
                  </Link>
                )}

                <Link to="/wishlist" className={`relative ${linkClass}`}>
                  <FiHeart className="text-xl" />
                  {wishlistCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold">
                      {wishlistCount}
                    </span>
                  )}
                </Link>

                <Link to="/cart" className={`relative ${linkClass}`}>
                  <FiShoppingCart className="text-xl" />
                  {cartCount > 0 && (
                    <span className="absolute -top-2 -right-2 bg-red-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center font-bold">
                      {cartCount}
                    </span>
                  )}
                </Link>

                <Link to="/profile" className={`flex items-center gap-2 ${linkClass}`}>
                  <FiUser className="text-xl" />
                  <span className="text-sm">{user.name}</span>
                </Link>
                <button
                  onClick={handleLogout}
                  className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg text-sm transition-colors"
                >
                  Logout
                </button>
              </>
            ) : (
              <>
                <Link to="/login" className={`text-sm ${linkClass}`}>Login</Link>
                <Link to="/register" className="bg-red-600 hover:bg-red-700 text-white px-4 py-2 rounded-lg text-sm transition-colors">
                  Register
                </Link>
              </>
            )}
          </div>

          <div className="md:hidden flex items-center gap-2">
            <button
              onClick={toggleTheme}
              className={`p-2 rounded-lg ${isDark ? 'text-gray-400' : 'text-gray-500'}`}
            >
              {isDark ? <FiSun /> : <FiMoon />}
            </button>
            <button
              className={`${isDark ? 'text-gray-300' : 'text-gray-600'} hover:text-white`}
              onClick={() => setMenuOpen(!menuOpen)}
            >
              {menuOpen ? <FiX className="text-2xl" /> : <FiMenu className="text-2xl" />}
            </button>
          </div>
        </div>
      </div>

      {menuOpen && (
        <div className={`md:hidden border-t px-4 py-4 flex flex-col gap-4 ${isDark ? 'bg-gray-900 border-gray-800' : 'bg-white border-gray-200'}`}>
          <Link to="/" onClick={() => setMenuOpen(false)} className={linkClass}>Home</Link>
          <Link to="/products" onClick={() => setMenuOpen(false)} className={linkClass}>Products</Link>
          <Link to="/contact" onClick={() => setMenuOpen(false)} className={linkClass}>Contact</Link>
          {user ? (
            <>
              <Link to="/wishlist" onClick={() => setMenuOpen(false)} className={`${linkClass} flex items-center gap-2`}>
                Wishlist {wishlistCount > 0 && <span className="bg-red-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">{wishlistCount}</span>}
              </Link>
              <Link to="/cart" onClick={() => setMenuOpen(false)} className={`${linkClass} flex items-center gap-2`}>
                Cart {cartCount > 0 && <span className="bg-red-600 text-white text-xs rounded-full w-5 h-5 flex items-center justify-center">{cartCount}</span>}
              </Link>
              <Link to="/profile" onClick={() => setMenuOpen(false)} className={linkClass}>Profile</Link>
              {isAdmin() && (
                <Link to="/admin" onClick={() => setMenuOpen(false)} className="text-yellow-400">Admin Panel</Link>
              )}
              <button onClick={handleLogout} className="bg-red-600 text-white px-4 py-2 rounded-lg text-sm text-left">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" onClick={() => setMenuOpen(false)} className={linkClass}>Login</Link>
              <Link to="/register" onClick={() => setMenuOpen(false)} className="bg-red-600 text-white px-4 py-2 rounded-lg text-sm">Register</Link>
            </>
          )}
        </div>
      )}
    </nav>
  )
}

export default Navbar
