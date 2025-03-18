<template>
  <div>
    <h1>{{ translations.createUserTitle }}</h1>
    <form @submit.prevent="createUser">
      <label for="username">{{ translations.username }}</label>
      <input type="text" v-model="username" required />
      <label for="email">{{ translations.email }}</label>
      <input type="email" v-model="email" required />
      <label for="password">{{ translations.password }}</label>
      <input type="password" v-model="password" required />
      <button type="submit">{{ translations.createUserButton }}</button>
    </form>
    <div v-if="user">
      <h2>{{ translations.newUserCreated }}</h2>
      <p><strong>ID:</strong> {{ user.id }}</p>
      <p>
        <strong>{{ translations.username }}:</strong> {{ user.username }}
      </p>
      <p>
        <strong>{{ translations.email }}:</strong> {{ user.email }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { defineProps } from "vue";

const props = defineProps({
  lang: String,
  translations: Object,
});

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
