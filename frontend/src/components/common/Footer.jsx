import { Link, useNavigate } from 'react-router-dom'
import { FiZap, FiMail, FiPhone, FiMapPin } from 'react-icons/fi'
import { FaFacebook, FaTwitter, FaInstagram, FaYoutube } from 'react-icons/fa'
import { useTheme } from '../../context/ThemeContext'
import { useEffect, useState } from 'react'
import API from '../../services/api'

const Footer = () => {
  const { isDark } = useTheme()
  const [categories, setCategories] = useState([])
  const navigate = useNavigate()

  useEffect(() => {
    API.get('/api/categories/all')
      .then(res => setCategories(res.data))
      .catch(err => console.error(err))
  }, [])

  const handleCategoryClick = (categoryId) => {
    navigate(`/products?category=${categoryId}`)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const t = isDark
    ? { bg: 'bg-gray-900', border: 'border-gray-800', text: 'text-gray-400', heading: 'text-white' }
    : { bg: 'bg-gray-100', border: 'border-gray-200', text: 'text-gray-500', heading: 'text-gray-900' }

  return (
    <footer className={`${t.bg} border-t ${t.border} mt-auto`}>
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">

          {/* Brand */}
          <div className="col-span-1">
            <div className="flex items-center gap-2 mb-4">
              <FiZap className="text-red-500 text-2xl" />
              <span className={`text-xl font-bold ${t.heading}`}>
                Electro<span className="text-red-500">Mart</span>
              </span>
            </div>
            <p className={`text-sm leading-relaxed mb-4 ${t.text}`}>
              Your trusted electronics store for the latest gadgets and technology at the best prices in Sri Lanka.
            </p>
            <div className="flex gap-4 mt-4">
              <FaFacebook className={`${t.text} hover:text-red-500 cursor-pointer text-xl transition-colors`} />
              <FaTwitter className={`${t.text} hover:text-red-500 cursor-pointer text-xl transition-colors`} />
              <FaInstagram className={`${t.text} hover:text-red-500 cursor-pointer text-xl transition-colors`} />
              <FaYoutube className={`${t.text} hover:text-red-500 cursor-pointer text-xl transition-colors`} />
            </div>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className={`font-semibold mb-4 ${t.heading}`}>Quick Links</h3>
            <ul className="space-y-2">
              <li><Link to="/" className={`${t.text} hover:text-red-500 text-sm transition-colors`}>Home</Link></li>
              <li><Link to="/products" className={`${t.text} hover:text-red-500 text-sm transition-colors`}>Products</Link></li>
              <li><Link to="/contact" className={`${t.text} hover:text-red-500 text-sm transition-colors`}>Contact Us</Link></li>
              <li><Link to="/register" className={`${t.text} hover:text-red-500 text-sm transition-colors`}>Create Account</Link></li>
            </ul>
          </div>

          {/* Categories — dynamically fetched & properly linked by ID */}
          <div>
            <h3 className={`font-semibold mb-4 ${t.heading}`}>Categories</h3>
            <ul className="space-y-2">
              {categories.length > 0
                ? categories.map(cat => (
                    <li key={cat.id}>
                      <button
                        onClick={() => handleCategoryClick(cat.id)}
                        className={`${t.text} hover:text-red-500 text-sm transition-colors text-left`}
                      >
                        {cat.name}
                      </button>
                    </li>
                  ))
                : ['Smartphone & Tab', 'Laptops', 'Audio & Headphones', 'Smart Watches', 'TV & Displays', 'Gaming & Consoles'].map(cat => (
                    <li key={cat}><span className={`text-sm ${t.text}`}>{cat}</span></li>
                  ))
              }
            </ul>
          </div>

          {/* Contact */}
          <div>
            <h3 className={`font-semibold mb-4 ${t.heading}`}>Contact Us</h3>
            <ul className="space-y-3">
              <li className={`flex items-center gap-2 text-sm ${t.text}`}>
                <FiMapPin className="text-red-500 flex-shrink-0" />
                <span>No 12, Navalar Road, Jaffna</span>
              </li>
              <li className={`flex items-center gap-2 text-sm ${t.text}`}>
                <FiPhone className="text-red-500 flex-shrink-0" />
                <span>+94 11 234 5678</span>
              </li>
              <li className={`flex items-center gap-2 text-sm ${t.text}`}>
                <FiMail className="text-red-500 flex-shrink-0" />
                <span>support@electromart.lk</span>
              </li>
            </ul>
          </div>
        </div>

        <div className={`border-t ${t.border} mt-8 pt-8 text-center`}>
          <p className={`text-sm ${t.text}`}>© 2026 ElectroMart. All rights reserved. Made with ❤️ in Sri Lanka</p>
        </div>
      </div>
    </footer>
  )
}

export default Footer
