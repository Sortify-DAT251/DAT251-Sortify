<template>
  <div>
    <h1>Create User</h1>
    <form @submit.prevent="createUser">
      <label for="username">Username:</label>
      <input type="text" v-model="username" required />
      <label for="email">Email:</label>
      <input type="email" v-model="email" required />
      <label for="password">Password:</label>
      <input type="password" v-model="password" required />
      <button type="submit">Create User</button>
    </form>
    <div v-if="user">
      <h2>New User Created</h2>
      <p><strong>ID:</strong> {{ user.id }}</p>
      <p><strong>Username:</strong> {{ user.username }}</p>
      <p><strong>Email:</strong> {{ user.email }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";

const username = ref("");
const email = ref("");
const password = ref("");
const user = ref(null);

const createUser = async () => {
  const userData = {
    username: username.value,
    email: email.value,
    password: password.value,
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
      user.value = data;
      console.log("User created:", data);
    } else {
      const error = await response.json();
      console.error("Error creating user:", error);
    }
  } catch (error) {
    console.error("Error:", error);
  }
};
</script>
