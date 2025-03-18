import { useEffect, useState } from "react";

const Home = () => {
  const [message, setMessage] = useState("Loading...");

  useEffect(() => {
    fetch("http://localhost:9876/api/message")
      .then((response) => response.json())
      .then((data) => setMessage(data.message))
      .catch((error) => console.error("Error fetching message:", error));
  }, []);

  return (
    <div id="test_container">
      <h1>Home page test</h1>
      <p>{message}</p>
      <p>
        Sortify is an application designed to make it easier for the user to
        recycle and dispose of waste. It includes an interactive map which
        displays collection points in an easy to read way. You can also compete
        against your friends and family to figure out who among you are better
        at recycling.
      </p>
    </div>
  );
};

export default Home;
