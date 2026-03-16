import React, { useState, useEffect } from 'react';
import { CategoryNavigation } from './CategoryNavigation';
import { ProductList } from './ProductList';
import { Footer } from './Footer';
import { Header } from './Header';
import './assets/styles.css';

export default function CustomerHomePage() {
  const [products, setProducts] = useState([]);
  const [cartCount, setCartCount] = useState(0);
  const [username, setUsername] = useState('');
  const [cartError, setCartError] = useState(false); // State for cart fetch error
  const [isCartLoading, setIsCartLoading] = useState(true); // State for cart loading

useEffect(() => {
  fetchProducts();
}, []);

  useEffect(() => {
    // fetchProducts();
    if (username) {
      fetchCartCount(); // Fetch cart count only if username is available
    }
  }, [username]); // Re-run cart count fetch if username changes

  const fetchProducts = async (category = '') => {
    try {
      console.log("FETCH CALLED");
      const response = await fetch(
        `http://localhost:9090/api/products${category ? `?category=${category}` : ''}`, 
        { credentials: 'include' } // Include authToken as a cookie
      );
      const data = await response.json();
      console.log("API RESPONSE:", data)
      // if(data)
    //  { 
      if(Array.isArray(data)) {
        setProducts(data);
        // return;
      } else if(Array.isArray(data.products)) {
        setProducts(data.products);
        setUsername(data.user?.name || 'Guest');
      }
      // if(Array.isArray(data.products)) {
      //   setProducts(data.products);
      //   setUsername(data.user?.name || 'Guest');
      //   return;
      // }
      // setUsername(data.user?.name || 'Guest'); // Extract username
      // setProducts(Array.isArray(data.products) ? data.products : []);
    // }else{
    //   setProducts([]);
// console.warn("Unexpected API structure");
    // }
    } catch (error) {
      console.error('Error fetching products:', error);
      // setProducts([]);
    }
  };

  const fetchCartCount = async () => {
    setIsCartLoading(true); // Set loading state
    try {
      const response = await fetch(`http://localhost:9090/api/cart/items/count?username=${username}`, {
        credentials: 'include', // Include authToken as a cookie
      });
      const count = await response.json();
      setCartCount(count);
      setCartError(false); // Reset error state if successful
    } catch (error) {
      console.error('Error fetching cart count:', error);
      setCartError(true); // Set error state
    } finally {
      setIsCartLoading(false); // Remove loading state
    }
  };

  const handleCategoryClick = (category) => {
    fetchProducts(category);
  };

  const handleAddToCart = async (productId) => {
    if (!username) {
      console.error('Username is required to add items to the cart');
      return;
    }
    try {
      const response = await fetch('http://localhost:9090/api/cart/add', {
        credentials: 'include',
        method: 'POST',
        body: JSON.stringify({ username, productId }), // Include username and productId in the request
        headers: { 'Content-Type': 'application/json' },
        // Include authToken as a cookie
      });

      if (response.ok) {
        fetchCartCount(); // Update cart count
      } else {
        console.error('Failed to add product to cart');
      }
    } catch (error) {
      console.error('Error adding product to cart:', error);
    }
  };

  return (
    <div className="customer-homepage">
      <Header
        cartCount={isCartLoading ? '...' : cartError ? 'Error' : cartCount}
        username={username}
      />
      <nav className="navigation">
        <CategoryNavigation onCategoryClick={handleCategoryClick} />
      </nav>
      <main className="main-content">
        <ProductList products={products} onAddToCart={handleAddToCart} />
      </main>
      <Footer />
    </div>
  );
}