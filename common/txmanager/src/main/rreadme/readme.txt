事务管理的设计思想

采用TCC模式
发起方先register事务到事务协调器，然后发起方调用execute，发起try操作，事务协调器返回try操作的结果。
对于发起方来说，拿到协调器返回的try操作的结果就算是完成了。其余的操作（confirm/cancel）将由事务协调器自动完成。
在事务执行过程中，发起方可以通过事务ID查询事务执行的情况。


对于事务接入方来说，要按照规定实现doTry/doConfirm/doCancel方法，以供事务协调器来调用。