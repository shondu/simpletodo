<template>
    <div>
        <h1>Whatcha thinking of doing?</h1>
        <hr class="hrpartial" v-if="items.length !== 0">
        <div class="title_bar" v-if="items.length !== 0">
            <span style="float: left; margin-right: 8px">Done</span>
            <span style="float: left">Item</span>
            <span style="float: right">Remove</span>
        </div>
        <br>
        <ul class="mainlist">
            <li v-for="item in items" :key="item.id" v-bind:class="{ item_completed : item.completed}">
                <input type="checkbox"
                       :id="item.id"
                       :value="item.id"
                       @click="itemClicked"
                       v-model="selectedItem"
                        class="done_check">
                {{item.todoValue}}
                <span @click="removeEntry(item.id)">x</span>
            </li>
        </ul>
        <hr class="hrpartial" v-if="items.length !== 0">
        <div class="input_div">
            <label for="newitem">What would you like to accomplish: </label>
            <input id="newitem" type="text" v-model="newItemValue" @keypress.enter="addItem()">
            <button v-if="items.length !== 0 && canUpdate" class="save_button" @click="saveData()">Save List</button>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: "todo_page",
        created() {
            this.serverCheck();
            if(this.useServer) {
                const todoUrl = 'http://localhost:9000/data/todos';
                axios.get(todoUrl).then(response => {
                    console.log(response)
                    this.items = response.data;
                    this.currentId = this.calculateCurrentId();
                }).catch(error => console.error(error));
            }else{
                const retrievedData = localStorage.getItem(this.todoItemString);
                if(retrievedData) {
                    this.items = JSON.parse(retrievedData);
                    this.currentId = this.calculateCurrentId();
                }
            }
        },
        data: function () {
            return {
                items: [],
                newItemValue: '',
                todoItemString: 'todoItems',
                currentId: 0,
                selectedItem: [],
                useServer: true,
                canUpdate: false
            }
        },
        methods: {
            addItem: function() {
                this.items.push({
                    id: this.currentId,
                    todoValue: this.newItemValue,
                    completed: false
                });
                this.newItemValue = '';
                this.currentId++;
                this.canUpdate = true;
                this.$emit('todo-event', {
                    message: 'Added An Item'
                })
            },
            removeEntry: function (itemIndex) {
                let foundIndex = -1;
                for(let index = 0; index < this.items.length; index++) {
                    if (this.items[index].id === itemIndex) {
                        foundIndex = index;
                        break;
                    }
                }

                if (foundIndex > -1) {
                    this.items.splice(foundIndex, 1);
                    this.$emit('todo-event', {
                        message: 'Removed An Item'
                    })
                    this.canUpdate = true;
                }
            },
            saveData: function () {
                this.canUpdate = false;
                if(this.useServer) {
                    const todoUrl = 'http://localhost:9000/data/todos';
                    axios.put(todoUrl, this.items)
                        .then(response => {
                            console.log(response)
                        })
                        .catch(error => console.error(error));
                } else {
                    if (this.items.length) {
                        localStorage.setItem(this.todoItemString, JSON.stringify(this.items));
                    }
                }
            },
            itemClicked: function($event) {
                let workItem = this.findItem($event.currentTarget.id);
                if (workItem !== null) {
                    workItem.completed = $event.currentTarget.checked;
                }
                this.canUpdate = true;
            },
            findItem: function(itemId){
                let returnItem = null;
                this.items.forEach(todoItem => {
                    if(todoItem.id === parseInt(itemId, 10)){
                        returnItem = todoItem;
                    }
                })

                return returnItem;
            },
            calculateCurrentId: function(){

                let returnedId = 0;

                this.items.forEach(todoItem => {
                    if(todoItem.completed){
                        this.selectedItem.push(todoItem.id);
                    }
                    if(todoItem.id > this.currentId) {
                        returnedId = todoItem.id;
                    }
                });

                returnedId++;
                return returnedId;

            },
            serverCheck: function() {
                let uri = window.location.href;
                if (uri.endsWith('local')) {
                    this.useServer=false;
                }
            }
        },
    }
</script>

<style scoped>
    .mainlist{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
        text-align: left;
        list-style-type: none;
    }
    .hrpartial {
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 60%;
    }
    .input_div{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 60%;
    }
    .save_button{
        float: right;
    }
    li > span {
        float: right;
        cursor: pointer;
        margin-right: 50px;
    }
    .title_bar{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
    }
    .item_completed {
        text-decoration: line-through;
    }
    .done_check {
        margin-left: -10px;
        margin-right: 15px;
    }
</style>