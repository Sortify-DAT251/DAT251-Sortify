import React, { useState, useEffect } from "react";
import styles from "./CreateUser.module.css";

interface CreateUserProps {
  lang: string;
  translations: {
    createUserTitle: string;
    username: string;
    email: string;
    password: string;
    createUserButton: string;
    newUserCreated: string;
    userListTitle: string;
  };
}

interface User {
  id: string;
  username: string;
  email: string;
}

const CreateUser: React.FC<CreateUserProps> = ({ lang, translations }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await fetch("http://localhost:9876/api/users");
      if (response.ok) {
        const data = await response.json();
        setUsers(data);
      } else {
        console.error("Error fetching users");
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  const createUser = async (event: React.FormEvent) => {
    event.preventDefault();
    const userData = {
      username,
      email,
      password,
    };

    try {
      const response = await fetch("http://localhost:9876/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      });

      if (response.ok) {
        const data = await response.json();
        setUsers([...users, data]);
        console.log("User created:", data);
      } else {
        const error = await response.json();
        console.error("Error creating user:", error);
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  return (
    <div className="register-content">
      <h1>{translations.createUserTitle}</h1>
      <form onSubmit={createUser} className={styles.form}>
        <label htmlFor="username" className={styles.label}>
          {translations.username}
        </label>
        <input
          type="text"
          id="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
          className={styles.input}
        />
        <label htmlFor="email" className={styles.label}>
          {translations.email}
        </label>
        <input
          type="email"
          id="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
          className={styles.input}
        />
        <label htmlFor="password" className={styles.label}>
          {translations.password}
        </label>
        <input
          type="password"
          id="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          className={styles.input}
        />
        <button type="submit" className={styles.button}>
          {translations.createUserButton}
        </button>
      </form>
      <h2>{translations.userListTitle}</h2>
      <ul>
        {users.map((user) => (
          <li key={user.id}>
            <p>
              <strong>ID:</strong> {user.id}
            </p>
            <p>
              <strong>{translations.username}:</strong> {user.username}
            </p>
            <p>
              <strong>{translations.email}:</strong> {user.email}
            </p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CreateUser;
