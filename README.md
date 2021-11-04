# Realm_CURD_operations
Realm Database CURD operations

Here i use **viewBinding.enabled= true**

<!-- Activity Binding -->
**ActivityMainBinding binding;
binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);**
        
<!-- dialog box binding -->
**AlertDialog.Builder builder = new AlertDialog.Builder(this);
DialogBinding dialogBinding= DialogBinding.inflate(getLayoutInflater());
builder.setView(deleteDataBinding.getRoot()); **   

**dialogBinding.name.setText("RaviKant");
AlertDialog alertDialog = builder.show();
**
