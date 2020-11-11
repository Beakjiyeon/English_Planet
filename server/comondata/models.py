# This is an auto-generated Django model module.
# You'll have to do the following manually to clean this up:
#   * Rearrange models' order
#   * Make sure each model has one field with primary_key=True
#   * Make sure each ForeignKey and OneToOneField has `on_delete` set to the desired behavior
#   * Remove `managed = False` lines if you wish to allow Django to create, modify, and delete the table
# Feel free to rename the models, but don't rename db_table values or field names.
from django.db import models


class Book(models.Model):
    p_id = models.IntegerField(blank=True, null=True)
    b_id = models.AutoField(primary_key=True)
    b_text = models.TextField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'book'


class Bookword(models.Model):
    bw_id = models.AutoField(primary_key=True)
    b_id = models.IntegerField()
    b_word_e = models.CharField(max_length=25)
    b_word_k = models.CharField(max_length=20)

    class Meta:
        managed = False
        db_table = 'bookword'


class Camera(models.Model):
    c_id = models.AutoField(primary_key=True)
    c_url = models.CharField(max_length=100)
    c_word_e = models.CharField(max_length=25)
    c_word_k = models.CharField(max_length=25)
    uid = models.CharField(max_length=20)

    class Meta:
        managed = False
        db_table = 'camera'


class Myword(models.Model):
    m_id = models.AutoField(primary_key=True)
    uid = models.CharField(max_length=20)
    m_word_e = models.CharField(max_length=25)
    m_word_k = models.CharField(max_length=25)

    class Meta:
        managed = False
        db_table = 'myword'


class Planet(models.Model):
    p_id = models.IntegerField(primary_key=True)
    p_progress = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'planet'


class Quiz(models.Model):
    q_id = models.AutoField(primary_key=True)
    b_id = models.IntegerField()
    q_word = models.CharField(max_length=20)
    q_sentence_e = models.TextField()
    q_sentence_k = models.TextField()

    class Meta:
        managed = False
        db_table = 'quiz'


class User(models.Model):
    uid = models.CharField(primary_key=True, max_length=20)
    upw = models.CharField(max_length=20)
    nickname = models.CharField(max_length=20)
    p_progress = models.IntegerField(blank=True, null=True)

    class Meta:
        managed = False
        db_table = 'user'
