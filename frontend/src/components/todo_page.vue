<template>
    <div>
        <h1>Whatcha thinking of doing?</h1>
        <hr class="hr-partial" v-if="items.length !== 0">
        <div class="title-bar" v-if="items.length !== 0">
            <span class="float-left space-right8">Done</span>
            <span class="float-left">Item</span>
            <span class="float-right">Remove</span>
        </div>
        <br>
        <ul class="todo-list">
            <li v-for="item in items"
                :key="item.id"
                :id="'listItem_' + item.id"
                :class="{ 'item-completed' : item.completed }"
                @click="listItemClicked">
                <input type="checkbox"
                       :id="item.id"
                       :value="item.id"
                       @click="itemClicked"
                       v-model="selectedItem"
                        class="done-check">
                <span :id="'itemDisplay_' + item.id">{{item.todoValue}}</span>
                <span :id="'itemEdit_' + item.id" style="display: none">
                    <input :id="'itemUpdate_' + item.id"
                           type="text"
                           v-model="currentEditItem"
                           @keypress.enter="updateItem" >
                </span>
                <span class="remove-link" :id="'itemRemove_'+item.id" @click="removeEntry">x</span>
            </li>
        </ul>
        <hr class="hr-partial" v-if="items.length !== 0">
        <div class="input-div">
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
                currentEditItem: '',
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
            removeEntry: function (event) {
                const itemIndex = document.getElementById(event.currentTarget.id).id.split('_')[1];
                event.stopPropagation();
                let foundIndex = -1;
                for(let index = 0; index < this.items.length; index++) {
                    if (this.items[index].id === parseInt(itemIndex, 10)) {
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
            itemClicked: function(event) {
                event.stopPropagation();
                const workItem = this.findItem(event.currentTarget.id);
                if (workItem !== null) {
                    workItem.completed = event.currentTarget.checked;
                }
                this.canUpdate = true;
            },
            listItemClicked(event){
                for(let itemIndex = 0; itemIndex < this.items.length; itemIndex++){
                    let workItemId = this.items[itemIndex];
                    document.getElementById('itemDisplay_' + workItemId.id).style.display = '';
                    document.getElementById('itemEdit_' + workItemId.id).style.display = 'none';
                }
                const itemId = document.getElementById(event.target.id).id.split('_')[1];
                const workItem = this.findItem(itemId);
                this.currentEditItem = workItem.todoValue;
                document.getElementById('itemEdit_' + itemId).style.display = "inline-block";
                document.getElementById('itemDisplay_' + itemId).style.display = 'none';
            },
            updateItem(event){
                const itemId = document.getElementById(event.target.id).id.split('_')[1];
                const workItem = this.findItem(itemId);
                workItem.todoValue = event.target.value;
                document.getElementById('itemDisplay_' + itemId).style.display = '';
                document.getElementById('itemEdit_' + itemId).style.display = 'none';
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
    .todo-list{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
        text-align: left;
        list-style-type: none;
    }
    .hr-partial {
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 60%;
    }
    .input-div{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 60%;
    }
    .save_button{
        float: right;
    }
    .title-bar{
        display: block;
        margin-left: auto;
        margin-right: auto;
        width: 50%;
    }
    .item-completed {
        text-decoration: line-through;
    }
    .done-check {
        margin-left: -10px;
        margin-right: 15px;
    }
    .remove-link{
        float: right;
        cursor: pointer;
        margin-right: 50px;
    }
    .float-left {
        float: left;
    }
    .float-right {
        float: right;
    }
    .space-right8{
        margin-right: 8px;
    }
</style>