const app = Vue.createApp({
    data(){
        return{
            darkMode:false,
            //SELECT
            selectAmountMax:0,
            selectName:'',
            selectPayments:[],
            //MESSAGE
            errorMessage:'',
            requestMessage:'',
        }
    },
    created(){
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
    },
    methods:{
        setDarkMode(){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode)); 
            document.querySelector('.btn-nav-close').click();      
        },
        createLoan(){
            this.errorMessage='';
            axios.post('/api/create/loans',`name=${this.selectName}&amountMax=${this.selectAmountMax}&payments=${this.selectPayments}`).then(res=>{
                this.requestMessage="Loan create";
                setTimeout(()=>location.reload(),1200);
            }).catch(e=>this.errorMessage=e.response.data);
        }
    }
})

let mount = app.mount('#app');