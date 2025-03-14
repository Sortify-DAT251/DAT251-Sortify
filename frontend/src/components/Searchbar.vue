<script setup>

import { ref } from 'vue';

const searchInput = ref('');
const testItems = [
  "Plastpose", "Plastflaske", "Plastbeger", "Plastbestikk", "Plastemballasje",
  "Plastfolie", "Plastkork", "Mykplast", "Hardplast", "Isopor",
  "Bobleplast", "Engangsplast", "PP-plast", "PET-flaske", "LDPE-plast", "PVC-rør",

  "Papirpose", "Pappeske", "Avispapir", "Reklamepapir", "Kartong",
  "Melkekartong", "Eggekartong", "Toalettpapirrull", "Tegnepapir",
  "Konvolutt", "Bokomslag", "Bølgepapp", "Serviett", "Matpapir",

  "Glassflaske", "Metallboks", "Hermetikkboks", "Syltetøyglass",
  "Vinflaske", "Ølflaske", "Metallfolie", "Aluminiumsboks",
  "Aluminiumsfolie", "Stålboks", "Lokk i metall", "Lysestake i glass",
  "Speil", "Drikkeboks",
  
  "Mobiltelefon", "Datamaskin", "Nettbrett", "Lader", "Batteri",
  "Litiumbatteri", "LED-lyspære", "Halogenpære", "Lysrør",
  "Kabel", "Høyttaler", "Hodetelefoner", "Fjernkontroll", "Strømledning",

  "Klær", "Sko", "Gardiner", "Sengetøy", "Håndklær",
  "Veske", "Belte", "Ullklær", "Jeans", "Skinnjakke",
  "Pledd", "Dyne", "Teppe", "Lær",

  "Maling", "Sprayboks", "Lakk", "Kjemikalier", "Rengjøringsmiddel",
  "Løsemidler", "Neglelakk", "Neglelakkfjerner", "Lim", "Blekkpatron",
  "Printerblekk", "Medisiner", "Desinfeksjonsmiddel", "Oljefilter",

  "Matrester", "Banan", "Epleskrott", "Kjøttrester", "Brødskalk",
  "Eggeskall", "Kaffegrut", "Tepose", "Fiskebein", "Grønnsaksskrell",
  "Nøtteskall", "Potetskrell", "Skall fra sitrusfrukt",

  "Gressklipp", "Løv", "Kvister", "Greiner", "Jord",
  "Planter", "Blomster", "Ugress", "Busker", "Hageavfall",


  "Stearinlys", "Tannbørste", "Q-tips", "Bomullspads",
  "Engangshanske", "Bleie", "Bind", "Tampong", "Snus", "Sigarettsneip",

  "Sofa", "Stol", "Bord", "Kommode", "Skap",
  "Seng", "Madrass", "Teppe", "Speil", "Sykkel",
  "Barnevogn", "TV", "Høyttaler"
];
const result = ref([]);

const searchResults = (itemList) => {
    const sortedResults = new Map();
    const input = searchInput.value.toLowerCase();
    let score = 1000;
    for (let i = 0; i < itemList.length; i++) {
        if (itemList[i].length < input.length-2) continue;
        const test = itemList[i].toLowerCase();

        if (test === input) {
            score = 0; // Eksakt match (best mulig treff)
        } else if (test.startsWith(input)) {
            score = 1; // Prefiks-match
        } else if (test.includes(input)) {
            score = 2; // Delvis match
        } else {
            score = 3 + levenshtein(input, test); // Fuzzy match
        }
    
        sortedResults.set(itemList[i], score) 
        
    }
    result.value = getSmallestKeys(sortedResults);
    console.log(sortedResults)
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

function getSmallestKeys(map, count = 5) {
  return [...map]
    .sort((a, b) => a[1] - b[1] || a[0].length - b[0].length) // Sort by value or length (ascending)
    .slice(0, count)             // Get first 'count' elements
    .map(([key]) => key);        // Extract keys
}
</script>

<template>
    <div>
        <input @keyup="searchResults(testItems)" type="text" v-model="searchInput" placeholder="Search..." />
        <span>{{ result }}</span>
    </div>
</template>

