<script setup>

import { ref } from 'vue';

const searchInput = ref('');
const testItems = ["Plastpose", "Papirpose", "vestland", "festland", "estland"]

const searchResults = () => {
    for (let i = 0; i < testItems.length; i++) {
        console.log("Search word:" + searchInput.value)
        console.log("Test word:" + testItems[i])
        console.log("Levenshtein distance: " + levenshtein(searchInput.value, testItems[i]))
    }
}

const levenshtein = (a, b) => {
    if (a.length === 0) return b.length; // if a is empty, return length of b
    if (b.length === 0) return a.length; // if b is empty, return length of a
    if (a[0] === b[0]) return levenshtein(a.slice(1,a.length), b.slice(1,b.length)) // if head of a and b are eaqual, return levenshtein of tail of a and b
    else {
        return 1 + Math.min(
            levenshtein(a.slice(1,a.length), b),
            levenshtein(a, b.slice(1,b.length)),
            levenshtein(a.slice(1,a.length), b.slice(1,b.length))
        )
    }
};
</script>

<template>
    <div>
        <input @keyup="searchResults" type="text" v-model="searchInput" placeholder="Search..." />
    </div>
</template>

