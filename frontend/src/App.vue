<template>
  <div id="app">
    <Todo_page v-on:todo-event="displayEventMessage"></Todo_page>
  </div>
</template>

<script>
import Todo_page from "@/components/todo_page";

export default {
  name: 'App',
  components: {
    Todo_page
  },
  methods: {
    displayEventMessage: function(event){
      console.log(event.message);
    }
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
