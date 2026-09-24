function App() {
  const apiBaseUrl = import.meta.env.VITE_API_BASE_URL;

  return (
    <div>
      <h1>Banking Application</h1>
      <p>API URL: {apiBaseUrl}</p>
    </div>
  );
}

export default App;